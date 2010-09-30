/*
 * Copyright (C) 2010 Texas Instruments Incorporated - http://www.ti.com/
 *
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *    Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the
 *    distribution.
 *
 *    Neither the name of Texas Instruments Incorporated nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
*/


//
// signGP.c
// Read the x-load.bin file and write out the x-load.bin.ift file.
// The signed image is the original pre-pended with the size of the image
// and the load address.  If not entered on command line, file name is
// assumed to be x-load.bin in current directory and load address is
// 0x40200800.

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <string.h>
#include <malloc.h>


main(int argc, char *argv[])
{
	int	i;
	char	ifname[FILENAME_MAX], ofname[FILENAME_MAX], ch;
	FILE	*ifile, *ofile;
	unsigned long	loadaddr, len;
	struct stat	sinfo;


	// Default to x-load.bin and 0x40200800.
	strcpy(ifname, "x-load.bin");
	loadaddr = 0x40200800;

	if ((argc == 2) || (argc == 3))
		strcpy(ifname, argv[1]);

	if (argc == 3)
		loadaddr = strtol(argv[2], NULL, 16);

	// Form the output file name.
	strcpy(ofname, ifname);
	strcat(ofname, ".ift");

	// Open the input file.
	ifile = fopen(ifname, "rb");
	if (ifile == NULL) {
		printf("Cannot open %s\n", ifname);
		exit(0);
	}

	// Get file length.
	stat(ifname, &sinfo);
	len = sinfo.st_size;

	// Open the output file and write it.
	ofile = fopen(ofname, "wb");
	if (ofile == NULL) {
		printf("Cannot open %s\n", ofname);
		fclose(ifile);
		exit(0);
	}

	// Pad 1 sector of zeroes.
	//ch = 0x00;
	//for (i=0; i<0x200; i++)
	//	fwrite(&ch, 1, 1, ofile);

	fwrite(&len, 1, 4, ofile);
	fwrite(&loadaddr, 1, 4, ofile);
	for (i=0; i<len; i++) {
		fread(&ch, 1, 1, ifile);
		fwrite(&ch, 1, 1, ofile);
	}

	fclose(ifile);
	fclose(ofile);
}
