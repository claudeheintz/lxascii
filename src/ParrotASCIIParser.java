//
//  ParrotASCIIParser.java
//
//  Created by Claude Heintz on 10/11/07.
//  Copyright (c) 2007-2014 Claude Heintz Design. All rights reserved.
//
/*
#
#   ParrotASCIIParser is free software: you can redistribute it and/or modify
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
#   ParrotASCIIParser.java is distributed in the hope that it will be useful,
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

public class ParrotASCIIParser extends AbstractASCIIParser {

	String _output;
	boolean _multifield;
	boolean _emptyline;
	
	public ParrotASCIIParser() {
		_output ="Ident 3:0\n";
		_multifield = false;
		_emptyline = true;
	}
	
	public String output() {
		return _output;
	}
	
	public void addToOutput(String s) {
		_emptyline = false;
		_output = _output + s;
	}
	
	public boolean inMultifieldLine() {
		return _multifield;
	}
	
	public void setInMultifieldLine(boolean b) {
		_multifield = b;
	}
	
	public boolean finishUnfinished() {
		addToOutput("\nenddata");
		return true;
	}
	
	public void finishProcessingLine() {
		if (! _emptyline ) {
			addToOutput("\n");
		}
		_emptyline = true;
		setInMultifieldLine(false);
		super.finishProcessingLine();
	}

	public void setConsole(String c) {
		addToOutput("Console " + c);
		super.setConsole(c);
	}
	public void setManufacturer(String m) {
		addToOutput("Manufacturer " + m);
		super.setManufacturer(m);
	}
	
	public void setCue(String c) {
		addToOutput("Cue " + c);
		super.setCue(c);
	}
	public void setCuePage(String c) {
		addToOutput(" " + c);
		super.setCuePage(c);
	}
	public void setGroup(String c) {
		addToOutput("Group " + c);
		super.setGroup(c);
	}
	public void setGroupPage(String c) {
		addToOutput(" " + c);
		super.setGroupPage(c);
	}
	public void setSub(String c) {
		if ( c != null ) {
			addToOutput("Sub " + c);
		}
		super.setSub(c);
	}
	public void setSubPage(String c) {
		if ( c != null ) {
			addToOutput(" " + c);
		}
		super.setSubPage(c);
	}
	
	public boolean doClear(String item, String page) {
		if ( page.equals("") ) {
			addToOutput("Clear " + item);
		} else {
			addToOutput("Clear " + item + " " + page);
		}
		return true;
	}
	
	public boolean doSet(String item, String value) {
		addToOutput("Set " + item + " " + value);
		return true;
	}
	
	public boolean doPatch(String page, String chan, String dim, String level) {
		if ( ! inMultifieldLine() ) {
			addToOutput("Patch " + page);
			setInMultifieldLine(true);
		}
		addToOutput(" " + chan + ">" + dim + "@" + level);
		return true;
	}
	
	public boolean doCueChan(String cue, String page, String chan, String level) {
		if ( ! inMultifieldLine() ) {
			addToOutput("Chan");
			setInMultifieldLine(true);
		}
		addToOutput(" " + chan + "@" + level);
		return true;
	}
	
	public boolean doGroupChan(String cue, String page, String chan, String level) {
		if ( ! inMultifieldLine() ) {
			addToOutput("Chan");
			setInMultifieldLine(true);
		}
		addToOutput(" " + chan + "@" + level);
		return true;
	}
	
	public boolean doSubChan(String cue, String page, String chan, String level) {
		if ( ! inMultifieldLine() ) {
			addToOutput("Chan");
			setInMultifieldLine(true);
		}
		addToOutput(" " + chan + "@" + level);
		return true;
	}
	
	public boolean doCueDown(String cue, String page, String time, String delay) {
		if ( delay.equals("") ) {
			addToOutput("Down " + time);
		} else {
			addToOutput("Down " + time + " " + delay);
		}
		return true;
	}
	
	public boolean doSubDown(String sub, String page, String time, String delay) {
		if ( delay.equals("") ) {
			addToOutput("Down " + time);
		} else {
			addToOutput("Down " + time + " " + delay);
		}
		return true;
	}
	
	public boolean doCueFollowon(String cue, String page, String time) {
		addToOutput("Followon " + time);
		return true;
	}
	
	public boolean doCueLink(String cue, String page, String link) {
		addToOutput("Link " + link);
		return true;
	}
	
	public boolean doCuePart(String cue, String page, String part) {
		addToOutput("Part " + part);
		return true;
	}
	
	public boolean doGroupPart(String group, String page, String part) {
		addToOutput("Part " + part);
		return true;
	}
	
	public boolean doCueText(String cue, String page, String text) {
		addToOutput("Text " + text);
		return true;
	}
	
	public boolean doGroupText(String group, String page, String text) {
		addToOutput("Text " + text);
		return true;
	}
	
	public boolean doSubText(String sub, String page, String text) {
		addToOutput("Text " + text);
		return true;
	}
	
	public boolean doCueUp(String cue, String page, String time, String delay) {
		if ( delay.equals("") ) {
			addToOutput("Up " + time);
		} else {
			addToOutput("Up " + time + " " + delay);
		}
		return true;
	}
	
	public boolean doSubUp(String sub, String page, String time, String delay) {
		if ( delay.equals("") ) {
			addToOutput("Up " + time);
		} else {
			addToOutput("Up " + time + " " + delay);
		}
		return true;
	}
	
}
