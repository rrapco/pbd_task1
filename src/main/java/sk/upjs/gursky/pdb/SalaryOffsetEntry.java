package sk.upjs.gursky.pdb;

import sk.upjs.gursky.bplustree.BPObject;

import java.nio.ByteBuffer;

public class SalaryOffsetEntry implements BPObject<SalaryKey, SalaryOffsetEntry> {

    private static final long serialVersionUID = -7681990658752380267L;

    int salary;

    long offset;

    public SalaryOffsetEntry(int salary, long offset) {
        this.salary = salary;
        this.offset = offset;
    }

    public SalaryOffsetEntry() {
    }

    @Override
    public void load(ByteBuffer bb) {
        salary = bb.getInt();
        offset = bb.getLong();
    }

    @Override
    public void save(ByteBuffer bb) {
        bb.putInt(salary);
        bb.putLong(offset);
    }

    @Override
    public int getSize() {
        return 12;
    }

    @Override
    public SalaryKey getKey() {
        return new SalaryKey(salary);
    }

    @Override
    public int compareTo(SalaryOffsetEntry salaryOffsetEntry) {
        if (this.salary > salaryOffsetEntry.salary) {
            return 1; // Táto inštancia je väčšia
        } else if (this.salary < salaryOffsetEntry.salary) {
            return -1; // Táto inštancia je menšia
        } else {
            return 0; // Sú rovnaké
        }
    }

    @Override
    public String toString() {
        return "SalaryOffsetEntry{" +
                "salary=" + salary +
                ", offset=" + offset +
                '}';
    }
}
