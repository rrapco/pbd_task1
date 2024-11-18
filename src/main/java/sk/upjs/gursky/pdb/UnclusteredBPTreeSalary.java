package sk.upjs.gursky.pdb;

import sk.upjs.gursky.bplustree.BPTree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UnclusteredBPTreeSalary extends BPTree<SalaryKey, SalaryOffsetEntry> {

    public static final long serialVersionUID = 5075293332434451865L;
    public static final File INDEX_FILE = new File("person.unclustered.tree");
    public static final File INPUT_DATA_FILE = new File("person.tab");


    public UnclusteredBPTreeSalary() throws IOException {
        super(SalaryOffsetEntry.class, INDEX_FILE); //index, subor na uloz stromu, subor na uloz metadat
    }

    public static UnclusteredBPTreeSalary createByBulkLoading() throws IOException {

        long startTime = System.nanoTime();
        UnclusteredBPTreeSalary tree = new UnclusteredBPTreeSalary();

        RandomAccessFile raf = new RandomAccessFile(INPUT_DATA_FILE, "r");

        FileChannel channel = raf.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

        List<SalaryOffsetEntry> entries = new ArrayList<>();
        long filesize = INPUT_DATA_FILE.length();

        for (int offset = 0; offset < filesize; offset += 4096) {
            System.out.println("procesing page " + (offset / 4096));

            buffer.clear();
            channel.read(buffer, offset);
            buffer.rewind();
            int numberOfRecords = buffer.getInt();
            for (int i = 0; i < numberOfRecords; i++) {
                PersonEntry entry = new PersonEntry();
                entry.load(buffer);
                long entryOffset = offset+4+entry.getSize()*i;
                SalaryOffsetEntry item = new SalaryOffsetEntry(entry.salary, entryOffset);
                entries.add(item);
            }
        }

        Collections.sort(entries);
        tree.openAndBatchUpdate(entries.iterator(), entries.size());
        channel.close();
        raf.close();
        System.out.println("unclustered index created in: " + (System.nanoTime() - startTime) / 1_000_000.0 + "ms");

        return tree;

    }
    public List<PersonEntry> unclusteredIntervalQuerySalary(SalaryKey low, SalaryKey high) throws IOException {
        List<SalaryOffsetEntry> references = intervalQuery(low,high);

        RandomAccessFile raf = new RandomAccessFile(INPUT_DATA_FILE, "r");

        FileChannel channel = raf.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
        List<PersonEntry> results = new LinkedList<>();
        long lastOffset = -1L;
        int access = 0;
        for (SalaryOffsetEntry ref : references) {
            long pageOffset = (ref.offset / 4096) * 4096;
            if (lastOffset != pageOffset){
                lastOffset = pageOffset;
                buffer.clear();
                channel.read(buffer, pageOffset);
                access++;
            }

            long entryInPageOffset = ref.offset - pageOffset;
            buffer.position((int) entryInPageOffset);
            PersonEntry entry = new PersonEntry();
            entry.load(buffer);
            results.add(entry);
        }

        channel.close();
        raf.close();
        System.out.println("I/O operations: " + access);
        return results;
    }

}
