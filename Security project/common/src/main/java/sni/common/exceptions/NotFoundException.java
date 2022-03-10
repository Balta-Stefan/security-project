package sni.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException
{
    public NotFoundException()
    {
        super(HttpStatus.NOT_FOUND);
    }


    public NotFoundException(Object data)
    {
        super(HttpStatus.NOT_FOUND, data);
    }

}