require xorg-util-common.inc

SUMMARY = "C preprocessor interface to the make utility"
DESCRIPTION = "Imake is used to generate Makefiles from a template, a \
set of cpp macro functions, and a per-directory input file called an \
Imakefile. This allows machine dependencies (such as compiler options, \
alternate command names, and special make rules) to be kept separate \
from the descriptions of the various items to be built."
LIC_FILES_CHKSUM = "file://COPYING;md5=b9c6cfb044c6d0ff899eaafe4c729367"

DEPENDS = "util-macros xproto xorg-cf-files"
RDEPENDS_${PN} = "perl xproto"

PR = "r0"
PE = "1"

SRC_URI[md5sum] = "60f4c648d9598603c2bc94b44efef4b2"
SRC_URI[sha256sum] = "e939695e46c26bc123065911c13d1210d062a54e3e01ea26e8ad20fa8a8e3b4f"

BBCLASSEXTEND = "native"
