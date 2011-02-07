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

PR = "r2"
PE = "1"

SRC_URI[md5sum] = "0fd1e53d94142ddee5340f87de0b9561"
SRC_URI[sha256sum] = "68038fa67929c5553044ad7417e1a64cabe954f04213b305dd36a04a61317d31"

BBCLASSEXTEND = "native"
