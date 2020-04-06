//
//  HexConverterASCIIParser.java
//
//  Created by Claude Heintz on 10/31/14
//  Copyright 2014-2020 Claude Heintz Design. All rights reserved.
//
/*
#
#   HexConverterASCIIParser is free software: you can redistribute it and/or modify
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
#   HexConverterASCIIParser.java is distributed in the hope that it will be useful,
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

public class HexConverterASCIIParser extends ParrotASCIIParser {

	public HexConverterASCIIParser() {
		
	}
	
	public int convertHexCharToInt( char c ) {
		if (c == '1' ) {
			return 1;
		} else if (c == '2' ) {
			return 2;
		} else if (c == '3' ) {
			return 3;
		} else if (c == '4' ) {
			return 4;
		} else if (c == '5' ) {
			return 5;
		} else if (c == '6' ) {
			return 6;
		} else if (c == '7' ) {
			return 7;
		} else if (c == '8' ) {
			return 8;
		} else if (c == '9' ) {
			return 9;
		} else if (c == 'a' ) {
			return 10;
		} else if (c == 'b' ) {
			return 11;
		} else if (c == 'c' ) {
			return 12;
		} else if (c == 'd' ) {
			return 13;
		} else if (c == 'e' ) {
			return 14;
		} else if (c == 'f' ) {
			return 15;
		} else if (c == 'A' ) {
			return 10;
		} else if (c == 'B' ) {
			return 11;
		} else if (c == 'C' ) {
			return 12;
		} else if (c == 'D' ) {
			return 13;
		} else if (c == 'E' ) {
			return 14;
		} else if (c == 'F' ) {
			return 15;
		}
		return 0;
	}
	
	public String convertHexString(String hs) {
		if ( hs.length() == 3 ) {
			char msb = hs.charAt(1);
			char lsb = hs.charAt(2);
			int hlv = convertHexCharToInt(msb) * 16 + convertHexCharToInt(lsb);
			int dlv = Math.round( hlv * (float)(100.0 / 255.0) );
			return Integer.toString(dlv);
		}
		return hs;
	}
	
	public boolean doPatch(String page, String chan, String dim, String level) {
		String clevel = level;
		if ( clevel.startsWith("h") || clevel.startsWith("H") ) {
			clevel = convertHexString(clevel);
		}
		return super.doPatch(page, chan, dim, clevel);
	}
	
	public boolean doCueChan(String cue, String page, String chan, String level) {
		String clevel = level;
		if ( clevel.startsWith("h") || clevel.startsWith("H") ) {
			clevel = convertHexString(clevel);
		}
		return super.doCueChan(cue, page, chan, clevel);
	}
	
	public boolean doGroupChan(String cue, String page, String chan, String level) {
		String clevel = level;
		if ( clevel.startsWith("h") || clevel.startsWith("H") ) {
			clevel = convertHexString(clevel);
		}
		return super.doGroupChan(cue, page, chan, clevel);
	}
	
	public boolean doSubChan(String cue, String page, String chan, String level) {
		String clevel = level;
		if ( clevel.startsWith("h") || clevel.startsWith("H") ) {
			clevel = convertHexString(clevel);
		}
		return super.doSubChan(cue, page, chan, clevel);
	}
	
}