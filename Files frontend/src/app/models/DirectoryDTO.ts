import { DirectoryBasicDTO } from "./DirectoryBasicDTO";
import { FileBasicDTO } from "./FileBasicDTO";

export interface DirectoryDTO{
    breadCrumbs: DirectoryBasicDTO[] | null;
    directory: DirectoryBasicDTO | null;
    children: FileBasicDTO[];
}