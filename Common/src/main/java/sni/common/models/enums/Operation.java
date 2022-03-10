package sni.common.models.enums;

public enum Operation
{
    CREATE_DIR("CD"),
    CREATE_FILE("CF"),
    UPDATE_FILE("UF"),
    RENAME_DIR("RD"),
    RENAME_FILE("RF"),
    DELETE_FILE("DF"),
    DELETE_DIRECTORY("DD"),
    MOVE("MV");

    public final String operation;

    Operation(String op)
    {
        this.operation = op;
    }
}
