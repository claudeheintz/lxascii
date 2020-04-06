//
//  PatchingASCIIParser.java
//  HexConverter
//
//  Created by Claude Heintz on 10/11/07.
//  Copyright (c) 2007-2014 Claude Heintz Design. All rights reserved.
//
/*
#
#   PatchingASCIIParser is free software: you can redistribute it and/or modify
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
#   PatchingASCIIParser.java is distributed in the hope that it will be useful,
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

public class PatchingASCIIParser extends ParrotASCIIParser {
	
	PatchTable _patchTable;
	
	public PatchingASCIIParser() {
		_patchTable = new PatchTable();
	}
	
	public PatchTable patchTable() {
		return _patchTable;
	}
	
	public boolean recognizedMfgBasic(String kw) {
		if (kw.equalsIgnoreCase("$$patchchan")) {
			return true;
		}
		if (kw.equalsIgnoreCase("$$patchchans")) {
			return true;
		}
		return false;
	}
	
	public boolean doClear(String item, String page) {
		if ( item.equalsIgnoreCase("$$chanpatch") ) {
			patchTable().clearTable();
		}
		return super.doClear(item, page);
	}
	
	public boolean doCueChan(String cue, String page, String chan, String level) {
		Vector v = patchTable().valuesForId(chan);
		String c;
		boolean b;
		
		if ((v.size()>1) && inMultifieldLine()) {
			addToOutput("\nChan ");
		}
		
		for(int i=0; i<v.size(); i++) {
			c = (String) v.elementAt(i);
			b = super.doCueChan(cue, page, c, level);
			if (!b) {
				return false;
			}
		}
		
		if (v.size()>1) {
			addToOutput("\nChan ");
		}
		
		return true;
	}
	
	public boolean doGroupChan(String cue, String page, String chan, String level) {
		Vector v = patchTable().valuesForId(chan);
		String c;
		boolean b;
		
		if ((v.size()>1) && inMultifieldLine()) {
			addToOutput("\nChan ");
		}
		
		for(int i=0; i<v.size(); i++) {
			c = (String) v.elementAt(i);
			b = super.doGroupChan(cue, page, c, level);
			if (!b) {
				return false;
			}
		}
		
		if (v.size()>1) {
			addToOutput("\nChan ");
		}
		
		return true;
	}
	
	public boolean doSubChan(String cue, String page, String chan, String level) {
		Vector v = patchTable().valuesForId(chan);
		String c;
		boolean b;
		
		if ((v.size()>1) && inMultifieldLine()) {
			addToOutput("\nChan ");
		}
		
		for(int i=0; i<v.size(); i++) {
			c = (String) v.elementAt(i);
			b = super.doSubChan(cue, page, c, level);
			if (!b) {
				return false;
			}
		}
		
		if (v.size()>1) {
			addToOutput("\nChan ");
		}
		
		return true;
	}
	
	public boolean doMfgBasic(Vector v) {
		if (((String)v.elementAt(0)).equalsIgnoreCase("$$patchchan")) {
			return keywordPatchChan(v);
		}
		if (((String)v.elementAt(0)).equalsIgnoreCase("$$patchchans")) {
			return keywordPatchChans(v);
		}
		return true;
	}
	
	public boolean keywordPatchChan(Vector v) {
		if ( v.size() > 2 ) {
			int rs = 3;
			while ( v.size()>=rs ) {
				patchTable().setValueForID( (String)v.elementAt(rs-1), (String)v.elementAt(rs-2));
				rs +=2;
			}
			return true;
		}
		addMessage("Warning:  bad PATCHCHAN ignored");
		return true;
	}
	
	public boolean keywordPatchChans(Vector v) {
		if ( v.size() > 2 ) {
			int rs = 3;
			while ( v.size()>=rs ) {
				patchTable().addValueForID( (String)v.elementAt(rs-1), (String)v.elementAt(1));
				rs +=1;
			}
			return true;
		}
		addMessage("Warning:  bad PATCHCHANS ignored");
		return true;
	}
	
}
