require qt4-tools-native.inc

LICENSE = "LGPLv2.1 | GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=fbc093901857fcd118f065f900982c24 \
                    file://LICENSE.GPL3;md5=babc5b6b77441da277f5c06b2e547720 \
                    file://LGPL_EXCEPTION.txt;md5=411080a56ff917a5a1aa08c98acae354"
PR = "r0"

EXTRA_OECONF += " -no-fast -silent -no-rpath"

TOBUILD := "src/tools/bootstrap ${TOBUILD}"
