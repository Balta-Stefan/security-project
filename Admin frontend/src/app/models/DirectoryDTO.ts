import { DirectoryBasicDTO } from "./DirectoryBasicDTO";
import { FileBasicDTO } from "./FileBasicDTO";

export interface DirectoryDTO{
    directory: DirectoryBasicDTO;
    children: FileBasicDTO[];
}