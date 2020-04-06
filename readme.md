# LXAsciiApp

The files in this package consist of the following


The files in this repository can be used to compile a swing javax application that demonstrates a parser that reads USITT ASCII text and then can perform one of two operations.

The generic AbstractASCII parser class reads an parses a USITT ASCII string.  However, to be useful, you must override the class.  Three examples of this are included.  The ParrotASCIIParser class will simply create an output string from the input string with no manipulation.  By Modifying this class, you can perform operations on the parsed data.  For example, the HexConverterASCIIParser class overrides ParrotASCIIParser and converts levels in hexadecimal to percentage in its output.

The PatchingASCIIParser class also overrides ParrotASCIIParser and allows you to build a table of channel substitutions that are then applied to your ASCII data.  The PatchTable and PatchTableEntry classes are used to translate channels as they are added to the output.

Replacing Channels
An advantage of USITT ASCII protocol is that it exposes the cue information for easy manipulation. You can, for example, copy cues from one show and paste them into another. This is a huge advantage in dance lighting where pieces from one concert are often included in another. The challenge to this type of use comes when the channels are not exactly identical from show to show or venue to venue.

How It Works
This operation requires that you build a table of replacement values by inserting statements into your input text. These statements follow manufacturer keyword guidelines as defined in the USITT standard. When the parser encounters a channel in cue, group or sub data, it looks in the table to see if there is a replacement or replacements specified. If so, it substitutes the original channel with the new value. In the case where there are multiple values for a channel in the table, the parser adds additional elements to the data.

This operation actually parses the ASCII Data. The messages view will inform you of exceptions or errors encountered in the operation. Also, the output text will be reformatted. The parser is fairly tolerant and will accept more than is strictly valid in the USITT standard. However, the it does require the channel data to be preceded by a valid Cue, Group or Sub record.

Table Building Statements

`$$patchchan` in_chan out_chan (in_chan out_chan)...
`$$Patchchan` adds or alters entries in the replacement table. For each input channel (in_chan) it stores the corresponding output value (out_chan).

`$$patchchans` in_chan out_chan (out_chan...)
`$$Patchchans` adds multiple entries into the replacement table. It has a single input channel (in_chan) and multiple output values (out_chan).

`Clear $$chanpatch`
This is an extension of the standard CLEAR record. It removes all entries from the replacement table.

Example

Input:
`$$patchchan 3>4`
`$$patchchans 2>2 3`
`cue 1`
`chan 1@25 2@50 3@75`
Output:
`Cue 1`
`Chan 1@25`
`Chan 2@50 3@50`
`Chan 4@75`

Although valid channels are necessary to produce output that can be used by a console, you can replace channels to make your cue data human friendly. Consider the following example:

Example

Input:
`$$patchchan 1>WarmSL 2>WarmCS 3>WarmSR`
`cue 1`
`chan 1@25 2@50 3@75`
Output:
`Cue 1`
`Chan WarmSL@25 WarmCS@50 WarmSR@75`