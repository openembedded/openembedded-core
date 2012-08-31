#
# Copyright (C) 2010  Intel Corporation. All rights reserved
#

DESCRIPTION = "Qt package groups"
LICENSE = "MIT"
PR = "r3"

inherit packagegroup

PACKAGES = "${PN}-demoapps"

QTDEMOS ?= "quicky ${COMMERCIAL_QT} fotowall"

SUMMARY_${PN}-demoapps = "Qt demo applications"
RDEPENDS_${PN}-demoapps = "${QTDEMOS}"
