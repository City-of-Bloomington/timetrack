package in.bloomington.timer;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobDataMap;
import java.util.List;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
//
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import in.bloomington.timer.util.*;
import in.bloomington.timer.timewarp.WarpEntry;
import in.bloomington.timer.report.ReasonReport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodeReasonJob implements Job{

    boolean debug = true;
		static final long serialVersionUID = 55L;		
		static Logger logger = LogManager.getLogger(CodeReasonJob.class);
		static String xlsOutputLocation = ""; 
		public CodeReasonJob(){

		}
		public void execute(JobExecutionContext context)
        throws JobExecutionException {
				try{
						JobDataMap dataMap = context.getJobDetail().getJobDataMap();
						if(dataMap != null){
								String val = (String) dataMap.get("outputFile");
								if(val != null){
									 xlsOutputLocation = val;
								}
						}
						doInit();
						doWork();
						doDestroy();
				}
				catch(Exception ex){
						logger.error(ex);
						System.err.println(ex);
				}
		}
		public void doInit(){

		}
		public void doDestroy() {

		}	    
		public void setOutputLocation(String val){
				if(val != null)
						xlsOutputLocation = val;
		}
    public void doWork(){
				List<WarpEntry> daily = null;
				String end_date = "02/01/2021";
				int cur_year = Helper.getCurrentYear();
				String start_date = "01/01/"+cur_year;
				ReasonReport report = new ReasonReport();
				report.setDate_from(start_date);
				report.setDate_to(end_date);
				String back = report.findHoursCodeDetails();
				if(back.isEmpty()){
						daily = report.getDailyEntries();
						writeXls(daily);
				}
				else{
						logger.error(back);
				}

		}
		void writeXls(List<WarpEntry> ones){
				// String file_output = xlsOutputLocation+"code_reason.xls";
				File file_output = prepareFile(xlsOutputLocation, "code_reason.xls");
				int cell_count = 6;
				int k=0;
				String headers[] = {"Full Name","Employee Numbe","Date","Earn Code","Reason","Hours"};
				try{
						HSSFWorkbook hwb = new HSSFWorkbook();
						HSSFSheet sheet = hwb.createSheet("new sheet");
						HSSFRow row = sheet.createRow((short) 0+k);
						for(int p=0;p<cell_count;p++){
								HSSFCell cell = row.createCell((short) p); // cell count
								cell.setCellType(CellType.STRING);
								cell.setCellValue(headers[p]);
						}
						int size = ones.size();
						for(WarpEntry one:ones){
								k++; // row number
								row = sheet.createRow((short) 0+k);
								int p = 0; // cell number
								HSSFCell cell = row.createCell((short) p);
								cell.setCellType(CellType.STRING);
								cell.setCellValue(one.getFullname());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellValue(one.getEmpNum());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.STRING);
								cell.setCellValue(one.getDate());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.STRING);
								cell.setCellValue(one.getCode());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.STRING);
								cell.setCellValue(one.getReason());
								p++;
								cell = row.createCell((short) p);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellValue(one.getHours());
								p++;
						}
						FileOutputStream fileOut = new FileOutputStream(file_output);
						hwb.write(fileOut);
						fileOut.close();
						setPermissions(file_output);
				}catch(Exception ex){
						System.err.println(ex);
						logger.error(ex);
				}
		}
		File prepareFile(String xlsOutputLocation, String fileName){
				String newFile = xlsOutputLocation+"/"+fileName;
				File outputFile = null;
				try{
						// check if dir exists first
						File dirName = new File(xlsOutputLocation);
						//
						Path filePath = Paths.get(newFile);
						// if file exists, delete it to replace with new one
						Files.deleteIfExists(filePath);
				}catch(Exception ex){
						System.err.println(ex);
				}
				try{
						outputFile = new File(newFile);
				}catch(Exception ex){
						System.err.println(ex);
				}
				return outputFile;
		}
		public void setPermissions(File file) throws IOException{
				Set<PosixFilePermission> perms = new HashSet<>();
				perms.add(PosixFilePermission.OWNER_READ);
				perms.add(PosixFilePermission.OWNER_WRITE);
				// perms.add(PosixFilePermission.OWNER_EXECUTE);
				
				perms.add(PosixFilePermission.OTHERS_READ);
				perms.add(PosixFilePermission.OTHERS_WRITE);
				// perms.add(PosixFilePermission.OTHERS_EXECUTE);
				
				perms.add(PosixFilePermission.GROUP_READ);
				perms.add(PosixFilePermission.GROUP_WRITE);
				// perms.add(PosixFilePermission.GROUP_EXECUTE);
				
				Files.setPosixFilePermissions(file.toPath(), perms);
		}
}






















































