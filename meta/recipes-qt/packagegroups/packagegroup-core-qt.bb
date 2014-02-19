#
# Copyright (C) 2010  Intel Corporation. All rights reserved
#

SUMMARY = "Qt package groups"
LICENSE = "MIT"
PR = "r4"

# Qt4 could NOT be built on MIPS64 with 64 bits userspace
COMPATIBLE_HOST_mips64 = "mips64.*-linux-gnun32"

inherit packagegroup

PACKAGES = "${PN}-demoapps"

# For backwards compatibility after rename
RPROVIDES_${PN}-demoapps = "task-core-qt-demos"
RREPLACES_${PN}-demoapps = "task-core-qt-demos"
RCONFLICTS_${PN}-demoapps = "task-core-qt-demos"

QTDEMOS ?= "quicky ${COMMERCIAL_QT} fotowall"

SUMMARY_${PN}-demoapps = "Qt demo applications"
RDEPENDS_${PN}-demoapps = "${QTDEMOS}"
