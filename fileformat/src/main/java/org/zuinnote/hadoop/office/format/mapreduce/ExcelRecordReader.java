/**
* Copyright 2016 ZuInnoTe (Jörn Franke) <zuinnote@gmail.com>
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

package org.zuinnote.hadoop.office.format.mapreduce;

import java.io.IOException;

import java.security.GeneralSecurityException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.RecordReader;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.zuinnote.hadoop.office.format.common.dao.SpreadSheetCellDAO;
import org.zuinnote.hadoop.office.format.common.parser.*;

/* ExcelRecordReader reads rows from Excel
*
* You can specify the following options:
* See AbstractTableDocumentRecordReader
*
*
*/


public class ExcelRecordReader extends AbstractSpreadSheetDocumentRecordReader<Text,ArrayWritable> {
private static final Log LOG = LogFactory.getLog(ExcelRecordReader.class.getName());
private FileSplit split;
private Text currentKey=new Text("");
private ArrayWritable currentValue=new ArrayWritable(SpreadSheetCellDAO.class);

public ExcelRecordReader(Configuration conf, FileSplit split) throws IOException,FormatNotUnderstoodException,GeneralSecurityException {
 super(conf);
 this.split=split;
}

/**
*
*  get current key after calling next()
*
* @return key is a text containing a reference for the SpreadSheet (e.g. [name.xlsx]Sheet1!A1)
*/
public Text getCurrentKey() {
	return this.currentKey;
}

/**
*
*  get current value after calling next()
*
* @return is an array of type SpreadSheetDAO
*/
public ArrayWritable getCurrentValue() {
	return this.currentValue;
}


/**
*
* Read row from Office document. If document does not match a defined metadata filter then it returns no rows. If no metadata filter is defined or document matches metadata filter then it returns rows, if available in the document/selected sheet
*
* @return true if next more rows are available, false if not
*/
public boolean nextKeyValue() throws IOException {
	if (this.getOfficeReader().getFiltered()==false) return false;
	Object[] objectArray = this.getOfficeReader().getNext();
	if (objectArray==null) return false; // no more to read
	SpreadSheetCellDAO[] cellRows = (SpreadSheetCellDAO[])objectArray;
	this.currentKey.set(new Text("["+this.split.getPath().getName()+"]"+this.getOfficeReader().getCurrentSheetName()+"!A"+this.getOfficeReader().getCurrentRow()));
	this.currentValue.set(cellRows);
	return true;	
}



}
