package sk.upjs.gursky.pdb;

import sk.upjs.gursky.bplustree.BPKey;

import java.nio.ByteBuffer;

public class SalaryKey implements BPKey<SalaryKey> {

    private static final long serialVersionUID = 5224522026678073893L;
    private int key;

    public SalaryKey() {}

    public SalaryKey(int key) {

        this.key = key;
    }

    public int getSize() {
        // int ma velkost 4
        return 4;
    }

    public void load(ByteBuffer bb) {
        key = bb.getInt();
    }

    public void save(ByteBuffer bb) {
        bb.putInt(key);
    }

    @Override
    public int compareTo(SalaryKey salaryKey) {

        if (this.key > salaryKey.key) {
            return 1;
        } else if (this.key < salaryKey.key) {
            return -1;
        } else {
            return 0;
        }
    }

}
