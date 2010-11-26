DESCRIPTION = "Pyrex is a language specially designed for writing Python extension modules. \
It's designed to bridge the gap between the nice, high-level, easy-to-use world of Python \
and the messy, low-level world of C."
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "Apache License Version 2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=771d472f53f933033f57eeee7808e5bd"
SRCNAME = "Pyrex"
PR = "ml0"

SRC_URI = "\
  http://www.cosc.canterbury.ac.nz/greg.ewing/python/${SRCNAME}/${SRCNAME}-${PV}.tar.gz \
  file://pyrex-fix-optimized-mode.patch \
"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils
