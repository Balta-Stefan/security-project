package sni.common.models.enums;

public enum Operation
{
    CREATE('C'),
    UPDATE('U'),
    DELETE('D'),
    MOVE('M');

    public final Character operation;

    Operation(Character op)
    {
        this.operation = op;
    }
}
