//
//  AbstractASCIIParser.java
//
//  Created by Claude Heintz on 10/8/07.
//  Copyright (c) 2007-2020 Claude Heintz Design. All rights reserved.
//
/*
#
#   AbstractASCIIParser is free software: you can redistribute it and/or modify
#   it for any purpose provided the following conditions are met:
#
#   1) Redistributions of source code must retain the above copyright notice,
#   this list of conditions and the following disclaimer.
#
#   2) Redistributions in binary form must reproduce the above copyright notice,
#   this list of conditions and the following disclaimer in the documentation
#   and/or other materials provided with the distribution.
#
#   3) Neither the name of the copyright owners nor the names of its contributors
#   may be used to endorse or promote products derived from this software
#   without specific prior written permission.
#
#   AbstractASCIIParser.java is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
#
#   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
#   INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
#   PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
#   HOLDERS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
#   CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
#   OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
#   AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
#   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
#   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import java.util.*;
import java.lang.*;

public class AbstractASCIIParser {
	static final int END_DATA = -1;
	static final int NO_PRIMARY = 0;
	static final int CUE_COLLECT = 1;
	static final int GROUP_COLLECT = 2;
	static final int SUB_COLLECT = 3;
	static final int MFG_COLLECT = 5;
	
	boolean _startedLine;
	int _line;
	int _state;
	String _currentLine;
	String _message;
	String _console;
	String _manufacturer;
	String _cue;
	String _group;
	String _sub;
	String _cuePage;
	String _groupPage;
	String _subPage;
	
	
	public AbstractASCIIParser() {
		_startedLine = false;
		_state = NO_PRIMARY;
		_line = 1;
		_message = "";
		_console = "";
		_manufacturer = "";
	}
	
	
	public String message() {
		return _message;
	}
	
	public void addMessage(String m) {
		_message = _message + "line" + Integer.toString(_line) + ":  " + m + '\n';
	}
	
	public int state() {
		return _state;
	}
	
	public void setState(int si) {
		_state = si;
	}
	
	public String console() {
		return _console;
	}
	
	public void setConsole(String c) {
		_console = c;
	}
	
	public String manufacturer() {
		return _manufacturer;
	}
	
	public void setManufacturer(String m) {
		_manufacturer = m;
	}
	
	public String cue() {
		return _cue;
	}
	
	public void setCue(String c) {
		_cue = c;
	}
	
	public String group() {
		return _group;
	}
	
	public void setGroup(String c) {
		_group = c;
	}
	
	public String sub() {
		return _sub;
	}
	
	public void setSub(String c) {
		_sub = c;
	}
	
	public String cuePage() {
		return _cuePage;
	}
	
	public void setCuePage(String c) {
		_cuePage = c;
	}
	
	public String groupPage() {
		return _groupPage;
	}
	
	public void setGroupPage(String c) {
		_groupPage = c;
	}
	
	public String subPage() {
		return _subPage;
	}
	
	public void setSubPage(String c) {
		_subPage = c;
	}
	
	public boolean recognizedMfgBasic(String kw) {
		return false;
	}
	
	public boolean recognizedMfgPrimary(String kw) {
		return false;
	}
	
	public boolean recognizedMfgSecondary(String kw) {
		return false;
	}
	
	public boolean processString(String s) {
		boolean valid = true;
		String cs = "";
		Vector lv = new Vector();
		System.out.println("processString() begin");
		
		for (int i=0; i<s.length(); i++) {
			cs = processCharacter(s.charAt(i), cs, lv);
			if ( cs == null)  {
				valid = false;
				break;
			}
			if ( state()==END_DATA ) {
				break;
			}
			System.out.println(cs);
		}
		
		if ( _startedLine ) {	//unfinished processing
			lv.add(cs);
			processLine(lv);
		}
		
		if ( valid && (state() != END_DATA) ) {
			addMessage("ER 0100 unexpected termination: ENDDATA missing");
			valid = finishUnfinished();
		} else {
			addMessage("0000 processing complete");
		}
		
		return valid;
	}
	
	public boolean finishUnfinished() {
		return false;
	}
	
	public String processCharacter(char c, String s, Vector v) {
		String rs = s;
		if ((c == '\n') || (c == '\r')) {						//end of line encountered
			if ( ! (s.equals("") && (v.size()==0) ) ) {			//ignore \n or \r if starting a new line (s is empty and v is empty)
				if ( ! (s.equals("!") || s.equals("") )   ) {	//if not a comment or null, add remaining  to vector b4 process
					v.add(s);
				}
				if ( processLine(v) ) {
					rs = "";
					v.removeAllElements();
					finishProcessingLine();
				} else {
					rs = null;// processLine is false, therfore procssing is aborted
				}
				_line++; //we'll go to the next line for error reporting
			}
		} else if ( ! (s.equals("!")))  {	//if comment (s.equals('!')), ignore or return s (!) until eol.
			if ( (c == '\t') || ((c>31) && (c<127)) ) {
				if ( AbstractASCIIParser.isDelimiter(c) ) {
					if ( ! ( s.equals("") || s.equals("!") ) ) {	//ignore leading delimiters and comments do if new delimit only
						rs="";
						v.add(s);	//we reached a delimiter with a string accumulated, add it to vector
					}
				} else {
					if (c=='!') {
						rs = "!"; //from ! to eol...
					} else {
						rs = s + String.valueOf( c );	//not a delimiter, add to string
						_startedLine = true;
					}
				}
			} else {
				addMessage("Invalid Character");//should abort by standard, we just ignore
			}
		}
		return rs;
	}
	
	public static boolean isDelimiter(char c) {
		if ( c == '\t' ) {
			return true;
		}
		if ( c == ' ' ) {
			return true;
		}
		if ( c == ',' ) {
			return true;
		}
		if ( c == '/' ) {
			return true;
		}
		if ( c == ';' ) {
			return true;
		}
		if ( c == '<' ) {
			return true;
		}
		if ( c == '=' ) {
			return true;
		}
		if ( c == '>' ) {
			return true;
		}
		if ( c == '@' ) {
			return true;
		}
		return false;
	}
	
	public boolean processLine(Vector v) {	//line is vector of strings
		if ( v.size() > 0 ) {
			String keyword = (String)v.elementAt(0);
			
			if (keyword.startsWith("$") ) {
				if ( ! keyword.startsWith("$$")) {
					return keywordMfgPrimary(v);
				} else if (state() != MFG_COLLECT) {
					return keywordMfgBasic(v);
				}
			}
			
			if ( keyword.length() > 10 ) {				//keywords are only significant first 10 chars
				keyword = keyword.substring(0,10);
			}
			
			if (keyword.equalsIgnoreCase("clear")) {	//check basic keywords first
				return keywordClear(v);
			}
			
			if (keyword.equalsIgnoreCase("console")) {
				return keywordConsole(v);
			}
			
			if (keyword.equalsIgnoreCase("enddata")) {
				setState(END_DATA);
				return true;
			}
			
			if (keyword.equalsIgnoreCase("ident")) {
				return keywordIdent(v);
			}
			
			if (keyword.equalsIgnoreCase("manufactur")) {
				return keywordManufacturer(v);
			}
			
			if (keyword.equalsIgnoreCase("patch")) {
				return keywordPatch(v);
			}
			
			if (keyword.equalsIgnoreCase("set")) {
				return keywordSet(v);
			}
			
			if (keyword.equalsIgnoreCase("cue")) {			//check primary keywords
				return keywordCue(v);
			}
			
			if (keyword.equalsIgnoreCase("group")) {
				return keywordGroup(v);
			}
			
			if (keyword.equalsIgnoreCase("sub")) {
				return keywordSub(v);
			}
			
			if (keyword.equalsIgnoreCase("text")) {
				return true;
			}
			
			if ( state() > MFG_COLLECT - 1 ) {
				return keywordMfgSecondary(v);
			}
			
			switch ( state() ) {
				case CUE_COLLECT:
					return keywordCueSecondary(v);
				case GROUP_COLLECT:
					return keywordGroupSecondary(v);
				case SUB_COLLECT:
					return keywordSubSecondary(v);
				default:
					addMessage("Warning:  unknown or out of place "+keyword+" ignored");
			}
		}
		
		return true;
	}
	
	public void finishProcessingLine() {
		//may be useful to subclass
		_startedLine = false;
	}
	
	public boolean keywordClear(Vector v) {
		if ( v.size() == 2 ) {
			return doClear((String)v.elementAt(1), "");
		}
		if ( v.size() == 3 ) {
			return doClear((String)v.elementAt(1), (String)v.elementAt(2));
		}
		addMessage("Warning:  bad CLEAR ignored");
		return true;		
	}
	
	public boolean keywordConsole(Vector v) {
		if ( v.size() == 2 ) {
			//by standard should ignore if no mfg yet
			setConsole((String)v.elementAt(1));
			return true;
		}
		addMessage("Warning:  bad CONSOLE ignored");
		return true;
	}
	
	public boolean keywordIdent(Vector v) {
		if ( v.size() == 2 ) {
			if (((String)v.elementAt(1)).equals("3:0")) {
				return true;
			} else {
				addMessage("ER 0099 Ident Mismatch");
			}
		}
		return false;
	}
	
	public boolean keywordManufacturer(Vector v) {
		if ( v.size() == 2 ) {
			setManufacturer((String)v.elementAt(1));
			return true;
		}
		addMessage("Warning:  bad MANUFACTURER ignored");
		return true;
	}
	
	public boolean keywordPatch(Vector v) {
		if ( v.size() > 4 ) {
			String page =(String) v.elementAt(1);
			boolean valid = true;
			int rs = 5;
			while ( valid && v.size()>=rs ) {
				valid = doPatch(page, (String)v.elementAt(rs-3),  (String)v.elementAt(rs-2), (String)v.elementAt(rs-1));
				rs +=3;
			}
			if ( valid ) {
				return true;
			}
		}
		addMessage("Warning:  bad PATCH ignored");
		return true;
	}
	
	public boolean keywordSet(Vector v) {
		if ( v.size() == 3 ) {
			return doSet((String)v.elementAt(1), (String)v.elementAt(2));
		}
		addMessage("Warning:  bad SET ignored");
		return true;
	}
	
	public boolean keywordCue(Vector v) {
		setState(CUE_COLLECT);
		if ( v.size() == 2 ) {
			setCue((String)v.elementAt(1));
			setCuePage("");
			return true;
		}
		if ( v.size() == 3 ) {
			setCue((String)v.elementAt(1));
			setCuePage((String)v.elementAt(2));
			return true;
		}
		setCue("");
		setCuePage("");
		addMessage("Warning:  bad CUE ignored");
		return true;
	}
	
	public boolean keywordCueSecondary(Vector v) {
		if ( ( ! cue().equals("") ) &&  ( v.size() > 0 ) ) {
			String keyword = (String)v.elementAt(0);
			
			if (keyword.startsWith("$$") ) {
				return keywordMfgForCue(v);
			}
			
			if ( keyword.length() > 10 ) {				//keywords are only significant first 10 chars
				keyword = keyword.substring(0,10);
			}
			
			if (keyword.equalsIgnoreCase("chan")) {
				return keywordChanForCue(v);
			}
			
			if (keyword.equalsIgnoreCase("down")) {
				return keywordDownForCue(v);
			}
			
			if (keyword.equalsIgnoreCase("followon")) {
				return keywordFollowonForCue(v);
			}
			
			if (keyword.equalsIgnoreCase("link")) {
				return keywordLinkForCue(v);
			}
			
			if (keyword.equalsIgnoreCase("part")) {
				return keywordPartForCue(v);
			}
			
			if (keyword.equalsIgnoreCase("text")) {
				return keywordTextForCue(v);
			}
			
			if (keyword.equalsIgnoreCase("up")) {
				return keywordUpForCue(v);
			}
			
			addMessage("Warning:  unknown or out of place "+keyword+" ignored");
		}
		
		return true;
	}
	
	public boolean keywordGroup(Vector v) {
		setState(GROUP_COLLECT);
		if ( v.size() == 2 ) {
			setGroup((String)v.elementAt(1));
			setGroupPage("");
			return true;
		}
		if ( v.size() == 3 ) {
			setGroup((String)v.elementAt(1));
			setGroupPage((String)v.elementAt(2));
			return true;
		}
		setGroup("");
		setGroupPage("");
		addMessage("Warning:  bad GROUP ignored");
		return true;
	}
	
	public boolean keywordGroupSecondary(Vector v) {
		if ( ( ! group().equals("") ) &&  ( v.size() > 0 ) ) {
			String keyword = (String)v.elementAt(0);
			
			if (keyword.startsWith("$$") ) {
				return keywordMfgForGroup(v);
			}
			
			if ( keyword.length() > 10 ) {				//keywords are only significant first 10 chars
				keyword = keyword.substring(0,10);
			}
			
			if (keyword.equalsIgnoreCase("chan")) {
				return keywordChanForGroup(v);
			}
			
			if (keyword.equalsIgnoreCase("part")) {
				return keywordPartForGroup(v);
			}
			
			if (keyword.equalsIgnoreCase("text")) {
				return keywordTextForGroup(v);
			}
			
			addMessage("Warning:  unknown or out of place "+keyword+" ignored");
		}
		return true;
	}
	
	public boolean keywordSub(Vector v) {
		setState(SUB_COLLECT);
		if ( v.size() == 2 ) {
			setSub((String)v.elementAt(1));
			setSubPage("");
			return true;
		}
		if ( v.size() == 3 ) {
			setSub((String)v.elementAt(1));
			setSubPage((String)v.elementAt(2));
			return true;
		}
		setSub("");
		setSubPage("");
		addMessage("Warning:  bad SUB ignored");
		return true;
	}
	
	public boolean keywordSubSecondary(Vector v) {
		if ( ( ! cue().equals("") ) &&  ( v.size() > 0 ) ) {
			String keyword = (String)v.elementAt(0);
			
			if (keyword.startsWith("$$") ) {
				return keywordMfgForGroup(v);
			}
			
			if ( keyword.length() > 10 ) {				//keywords are only significant first 10 chars
				keyword = keyword.substring(0,10);
			}
			
			if (keyword.equalsIgnoreCase("chan")) {
				return keywordChanForSub(v);
			}
			
			if (keyword.equalsIgnoreCase("down")) {
				return keywordDownForSub(v);
			}
			
			if (keyword.equalsIgnoreCase("text")) {
				return keywordTextForSub(v);
			}
			
			if (keyword.equalsIgnoreCase("up")) {
				return keywordUpForSub(v);
			}
			
			addMessage("Warning:  unknown or out of place "+keyword+" ignored");
		}
		return true;
	}
	
	public boolean keywordChanForCue(Vector v) {
		if ( v.size() > 2 ) {
			boolean valid = true;
			int rs = 3;
			while ( valid && v.size()>=rs ) {
				valid = doCueChan(cue(), cuePage(), (String)v.elementAt(rs-2), (String)v.elementAt(rs-1));
				rs +=2;
			}
			if ( valid ) {
				return true;
			}
		}
		addMessage("Warning:  bad CHAN ignored");
		return true;
	}
	
	public boolean keywordChanForGroup(Vector v) {
		if ( v.size() > 2 ) {
			boolean valid = true;
			int rs = 3;
			while ( valid && v.size()>=rs ) {
				valid = doGroupChan(group(), groupPage(), (String)v.elementAt(rs-2), (String)v.elementAt(rs-1));
				rs +=2;
			}
			if ( valid ) {
				return true;
			}
		}
		addMessage("Warning:  bad CHAN ignored");
		return true;
	}
	
	public boolean keywordChanForSub(Vector v) {
		if ( v.size() > 2 ) {
			boolean valid = true;
			int rs = 3;
			while ( valid && v.size()>=rs ) {
				valid = doSubChan(sub(), subPage(), (String)v.elementAt(rs-2), (String)v.elementAt(rs-1));
				rs +=2;
			}
			if ( valid ) {
				return true;
			}
		}
		addMessage("Warning:  bad CHAN ignored");
		return true;
	}
	
	public boolean keywordDownForCue(Vector v) {
		if ( v.size() == 2 ) {
			return doCueDown(cue(), cuePage(), (String)v.elementAt(1), "");
		}
		if ( v.size() == 3 ) {
			return doCueDown(cue(), cuePage(), (String)v.elementAt(1), (String)v.elementAt(2));
		}
		addMessage("Warning:  bad DOWN ignored");
		return true;
	}
	
	public boolean keywordDownForSub(Vector v) {
		if ( v.size() == 2 ) {
			return doSubDown(sub(), subPage(), (String)v.elementAt(1), "");
		}
		if ( v.size() == 3 ) {
			return doSubDown(sub(), subPage(), (String)v.elementAt(1), (String)v.elementAt(2));
		}
		addMessage("Warning:  bad DOWN ignored");
		return true;
	}
	
	public boolean keywordFollowonForCue(Vector v) {
		if ( v.size() == 2 ) {
			return doCueFollowon(cue(), cuePage(), (String)v.elementAt(1));
		}
		addMessage("Warning:  bad FOLLOWON ignored");
		return true;
	}
	
	public boolean keywordLinkForCue(Vector v) {
		if ( v.size() == 2 ) {
			return doCueLink(cue(), cuePage(), (String)v.elementAt(1));
		}
		addMessage("Warning:  bad LINK ignored");
		return true;
	}
	
	public boolean keywordPartForCue(Vector v) {
		if ( v.size() == 2 ) {
			return doCuePart(cue(), cuePage(), (String)v.elementAt(1));
		}
		addMessage("Warning:  bad PART ignored");
		return true;
	}
	
	public boolean keywordPartForGroup(Vector v) {
		if ( v.size() == 2 ) {
			return doGroupPart(group(), groupPage(), (String)v.elementAt(1));
		}
		addMessage("Warning:  bad PART ignored");
		return true;
	}
	
	public boolean keywordTextForCue(Vector v) {
		if ( v.size() > 1 ) {
			String text ="";
			int rs = 1;
			while ( v.size()>=rs ) {
				if ( rs  > 1 ) {
					text = text + " ";
				}
				text=text+v.elementAt(rs-1);
			}
			return doCueText(cue(), cuePage(), text);
		}
		return true;
	}
	
	public boolean keywordTextForGroup(Vector v) {
		if ( v.size() >1 ) {
			String text ="";
			int rs = 1;
			while ( v.size()>=rs ) {
				if ( rs  > 1 ) {
					text = text + " ";
				}
				text=text+v.elementAt(rs-1);
			}
			return doGroupText(group(), groupPage(), text);
		}
		return true;
	}
	
	public boolean keywordTextForSub(Vector v) {
		if ( v.size() >1 ) {
			String text ="";
			int rs = 1;
			while ( v.size()>=rs ) {
				if ( rs  > 1 ) {
					text = text + " ";
				}
				text=text+v.elementAt(rs-1);
			}
			return doSubText(sub(), subPage(), text);
		}
		return true;
	}
	
	public boolean keywordUpForCue(Vector v) {
		if ( v.size() == 2 ) {
			return doCueUp(cue(), cuePage(), (String)v.elementAt(1), "");
		}
		if ( v.size() == 3 ) {
			return doCueUp(cue(), cuePage(), (String)v.elementAt(1), (String)v.elementAt(2));
		}
		addMessage("Warning:  bad UP ignored");
		return true;
	}
	
	public boolean keywordUpForSub(Vector v) {
		if ( v.size() == 2 ) {
			return doSubUp(sub(), subPage(), (String)v.elementAt(1), "");
		}
		if ( v.size() == 3 ) {
			return doSubUp(sub(), subPage(), (String)v.elementAt(1), (String)v.elementAt(2));
		}
		addMessage("Warning:  bad UP ignored");
		return true;
	}
	
	public boolean keywordMfgForCue(Vector v) {
		if ( recognizedMfgBasic((String) v.elementAt(0)) ) {
			return doMfgBasic(v);
		}
		if ( recognizedMfgSecondary((String) v.elementAt(0)) ) {
			return doCueMfgSecondary(cue(), cuePage(), v);
		}
		return true;
	}
	
	public boolean keywordMfgForGroup(Vector v) {
		if ( recognizedMfgBasic((String) v.elementAt(0)) ) {
			return doMfgBasic(v);
		}
		if ( recognizedMfgSecondary((String) v.elementAt(0)) ) {
			return doGroupMfgSecondary(group(), groupPage(), v);
		}
		return true;
	}
	
	public boolean keywordMfgForSub(Vector v) {
		if ( recognizedMfgBasic((String) v.elementAt(0)) ) {
			return doMfgBasic(v);
		}
		if ( recognizedMfgSecondary((String) v.elementAt(0)) ) {
			return doSubMfgSecondary(sub(), subPage(), v);
		}
		return true;
	}
	
	public boolean keywordMfgBasic(Vector v) {
		if ( recognizedMfgBasic((String) v.elementAt(0)) ) {
			return doMfgBasic(v);
		}
		return true;
	}
	
	public boolean keywordMfgPrimary(Vector v) {
		setState(MFG_COLLECT);
		if ( recognizedMfgBasic((String) v.elementAt(0)) ) {
			return doMfgPrimary(v);
		}
		return true;
	}
	
	public boolean keywordMfgSecondary(Vector v) {
		if ( recognizedMfgSecondary((String) v.elementAt(0)) ) {
			return doMfgSecondary(v);
		}
		return true;
	}
	
	public boolean doClear(String item, String page) {
		//implemented by subclass
		return true;
	}
	
	public boolean doSet(String item, String value) {
		//implemented by subclass
		return true;
	}
	
	public boolean doPatch(String page, String chan, String dim, String level) {
		//implemented by subclass
		return true;
	}
	
	public boolean doCueChan(String cue, String page, String chan, String level) {
		//implemented by subclass
		return true;
	}
	
	public boolean doGroupChan(String cue, String page, String chan, String level) {
		//implemented by subclass
		return true;
	}
	
	public boolean doSubChan(String cue, String page, String chan, String level) {
		//implemented by subclass
		return true;
	}
	
	public boolean doCueDown(String cue, String page, String time, String delay) {
		//implemented by subclass
		return true;
	}
	
	public boolean doSubDown(String sub, String page, String time, String delay) {
		//implemented by subclass
		return true;
	}
	
	public boolean doCueFollowon(String cue, String page, String time) {
		//implemented by subclass
		return true;
	}
	
	public boolean doCueLink(String cue, String page, String link) {
		//implemented by subclass
		return true;
	}
	
	public boolean doCuePart(String cue, String page, String part) {
		//implemented by subclass
		return true;
	}
	
	public boolean doGroupPart(String group, String page, String part) {
		//implemented by subclass
		return true;
	}
	
	public boolean doCueText(String cue, String page, String text) {
		//implemented by subclass
		return true;
	}
	
	public boolean doGroupText(String group, String page, String text) {
		//implemented by subclass
		return true;
	}
	
	public boolean doSubText(String sub, String page, String text) {
		//implemented by subclass
		return true;
	}
	
	public boolean doCueUp(String cue, String page, String time, String delay) {
		//implemented by subclass
		return true;
	}
	
	public boolean doSubUp(String sub, String page, String time, String delay) {
		//implemented by subclass
		return true;
	}
	
	public boolean doCueMfgSecondary(String cue, String page, Vector v) {
		//implemented by subclass
		return true;
	}
	
	public boolean doGroupMfgSecondary(String group, String page, Vector v) {
		//implemented by subclass
		return true;
	}
	
	public boolean doSubMfgSecondary(String sub, String page, Vector v) {
		//implemented by subclass
		return true;
	}
	
	public boolean doMfgBasic(Vector v) {
		//implemented by subclass
		return true;
	}
	
	public boolean doMfgPrimary(Vector v) {
		//implemented by subclass
		return true;
	}
	
	public boolean doMfgSecondary(Vector v) {
		//implemented by subclass
		return true;
	}

}
