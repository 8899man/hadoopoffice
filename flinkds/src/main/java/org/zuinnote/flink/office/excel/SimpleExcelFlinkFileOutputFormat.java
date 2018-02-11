/**
* Copyright 2017 ZuInnoTe (Jörn Franke) <zuinnote@gmail.com>
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
**/
package org.zuinnote.flink.office.excel;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zuinnote.flink.office.AbstractSpreadSheetFlinkFileOutputFormat;
import org.zuinnote.hadoop.office.format.common.HadoopOfficeWriteConfiguration;
import org.zuinnote.hadoop.office.format.common.converter.ExcelConverterSimpleSpreadSheetCellDAO;
import org.zuinnote.hadoop.office.format.common.dao.SpreadSheetCellDAO;
import org.zuinnote.hadoop.office.format.common.writer.OfficeWriterException;

/**
 * @author jornfranke
 *
 */
public class SimpleExcelFlinkOutputFormat extends AbstractSpreadSheetFlinkFileOutputFormat<Object[]> {
	
	

	private static final Log LOG = LogFactory.getLog(SimpleExcelFlinkOutputFormat.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 8528766434712667829L;
	private ExcelConverterSimpleSpreadSheetCellDAO converter;
	private String sheetName;
	private int rowNum;
	
	public SimpleExcelFlinkOutputFormat(HadoopOfficeWriteConfiguration howc, String sheetName, ExcelConverterSimpleSpreadSheetCellDAO converter) {
		super(howc);
		this.converter=converter;
		this.sheetName=sheetName;
		this.rowNum=0;
	}

	@Override
	public void writeRecord(Object[] record) throws IOException {
		try {
			this.getOfficeWriter().write(converter.getSpreadSheetCellDAOfromSimpleDataType(record, this.sheetName, rowNum));
		} catch (OfficeWriterException e) {
			LOG.error(e);
		}
		this.rowNum++;
		
	}

}