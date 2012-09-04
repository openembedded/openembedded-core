#
# Copyright (C) 2010  Intel Corporation. All rights reserved
#

DESCRIPTION = "Qt package groups"
LICENSE = "MIT"
PR = "r4"

inherit packagegroup

PACKAGES = "${PN}-demoapps"

# For backwards compatibility after rename
RPROVIDES_${PN}-demoapps = "task-core-qt-demos"
RREPLACES_${PN}-demoapps = "task-core-qt-demos"
RCONFLICTS_${PN}-demoapps = "task-core-qt-demos"

QTDEMOS ?= "quicky ${COMMERCIAL_QT} fotowall"

SUMMARY_${PN}-demoapps = "Qt demo applications"
RDEPENDS_${PN}-demoapps = "${QTDEMOS}"
