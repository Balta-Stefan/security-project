import { FileBasicDTO } from "./FileBasicDTO";
import { Operation } from "./Operation";
import { UserBasicDTO } from "./UserBasicDTO";

export interface FileLogDTO{
    logId: number;
    description: string;
    timestamp: Date;
    operation: Operation;
    affectedFile: FileBasicDTO;
    createdBy: UserBasicDTO;
}