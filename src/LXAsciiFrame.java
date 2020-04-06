//
//  LXAsciiFrame.java
//  LXAsciiApp
//
//  Created by Claude Heintz on 10/8/07.
//  Copyright (c) 2007-2020 Claude Heintz Design. All rights reserved.
//
//  Converted from applet to Swing JFrame on 4/6/20
//
/*
#
#   LXAsciiFrame is free software: you can redistribute it and/or modify
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
#   ASCIIApplet.java is distributed in the hope that it will be useful,
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

import java.awt.*;
import java.applet.*;
import javax.swing.*;

public class LXAsciiFrame extends JFrame {
	
	javax.swing.JTextArea jtaAscii;
	javax.swing.JComboBox jcbProcess;
	javax.swing.JComboBox jcbView;
	String _input;
	String _output;
	String _error;
	int _view;
	
	public LXAsciiFrame() {
		init();
	}

	public LXAsciiFrame(String s) {
		super(s);

		init();
	}
	
    public void init() {
       getContentPane().setLayout (null);
		setSize(new java.awt.Dimension(600, 400));
		setLocation(new java.awt.Point(0, 0));
		
		jcbProcess = new javax.swing.JComboBox();
		jcbProcess.addItem( "Convert Hex to %" );
		jcbProcess.addItem( "Parse for Chan Transpose" );
		jcbProcess.setSize(new java.awt.Dimension(200, 20));
		jcbProcess.setVisible(true);
		jcbProcess.setLocation(new java.awt.Point(20, 270));
		getContentPane().add(jcbProcess);
		
		javax.swing.JButton jbProcess = new javax.swing.JButton();
		jbProcess.setSize(new java.awt.Dimension(75, 20));
		jbProcess.setVisible(true);
		jbProcess.setText("Go");
		jbProcess.setLocation(new java.awt.Point(240, 270));
		getContentPane().add(jbProcess);
		jbProcess.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jbProcessActionPerformed(e);
			}
		});
		
		jcbView = new javax.swing.JComboBox();
		jcbView.addItem( "Input" );
		jcbView.addItem( "Output" );
		jcbView.addItem( "Messages" );
		jcbView.addItem( "-" );
		jcbView.addItem( "Reset" );
		jcbView.setSize(new java.awt.Dimension(120, 20));
		jcbView.setVisible(true);
		jcbView.setLocation(new java.awt.Point(460, 270));
		getContentPane().add(jcbView);
		jcbView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jcbViewActionPerformed(e);
			}
		});
		
		jtaAscii = new javax.swing.JTextArea();
		jtaAscii.setFont(new Font("Monospaced", 0, 12));
		javax.swing.JScrollPane jspAscii = new JScrollPane(jtaAscii);
		jspAscii.setSize(new java.awt.Dimension(580, 250));
		jspAscii.setLocation(new java.awt.Point(10, 10));
		getContentPane().add(jspAscii);
		
		doClearView();
		setVisible(true);
    }
	
	public void jcbViewActionPerformed(java.awt.event.ActionEvent e) {
		if ( _view == 0 ) {
			_input = jtaAscii.getText();
		}
		switch (jcbView.getSelectedIndex()) {
			case 0:
				jtaAscii.setText(_input);
				_view = 0;
				break;
			case 1:
				jtaAscii.setText(_output);
				_view = 1;
				break;
			case 2:
				jtaAscii.setText(_error);
				_view = 2;
				break;
			case 4:
				doClearView();
				break;
		}
	}
	
	public void jbProcessActionPerformed(java.awt.event.ActionEvent e) {
		switch (jcbProcess.getSelectedIndex()) {
			case 0:
				doConvertHexToPercent();
				break;
			case 1:
				doParseForTransposition();
				break;
		}
	}
	
	public void doConvertHexToPercent() {
		if ( _view == 0 ) {
			_input = jtaAscii.getText();
		}
		HexConverterASCIIParser h2p = new HexConverterASCIIParser();
		System.out.print("will process...");
		h2p.processString(_input);
		System.out.println(" processed");
		_output = h2p.output();
		_error = h2p.message();
		jcbView.setSelectedIndex(1);
	}
	
	public void doParseForTransposition() {
		if ( _view == 0 ) {
			_input = jtaAscii.getText();
		}
		PatchingASCIIParser pap = new PatchingASCIIParser();
		pap.processString(_input);
		_output = pap.output();
		_error = pap.message();
		jcbView.setSelectedIndex(1);
	}
	
	public void doClearView() {
		_view = -1;
		_input = "Paste Ascii Data Here";
		_output = "";
		_error = "Copyright 2007 & 2020 by Claude Heintz Design, All Rights Reserved";
		jcbView.setSelectedIndex(0);
		jtaAscii.setText(_input);
		_view = 0;
	}
	
	// for java script bridge to applet *********************************
	
	public void doJSConvertHexToPercent() {
		HexConverterASCIIParser h2p = new HexConverterASCIIParser();
		h2p.processString(_input);
		_output = h2p.output();
		_error = h2p.message();
	}
	
	public void doJSParseForTransposition() {		
		PatchingASCIIParser pap = new PatchingASCIIParser();
		pap.processString(_input);
		_output = pap.output();
		_error = pap.message();
	}
	
	public void doJSClearView() {
		_input = "Paste Ascii Data Here";
		_output = "";
		_error = "Copyright 2007 by Claude Heintz Design, All Rights Reserved";
	}
	
	public void setInput(String ts) {
		_input =ts;
	}
	
	public String getInput() {
		return(_input);
	}
	
	public String getOutput() {
		return(_output);
	}
	
	public String getError() {
		return(_error);
	}
}
