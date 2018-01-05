package com.softwareag.mediator.server.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Sample to check multipart form data content type
 * @author samuel
 *
 */
@Singleton
@Path("/file")
public class FileUpload {
	
	@Path("/push")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response storeFileFromGet(
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail) {
		
		String uploadedFileLocation = "c:/tmp/uploaded/" + fileDetail.getFileName();
		writeToFile(uploadedInputStream, uploadedFileLocation);
		return Response.status(Status.CREATED).build();
	}
	
	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
		String uploadedFileLocation) {
 
		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
 
			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
