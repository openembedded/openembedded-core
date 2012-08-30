#
# Copyright (C) 2010  Intel Corporation. All rights reserved
#

DESCRIPTION = "Qt Tasks for Poky"
LICENSE = "MIT"
PR = "r3"

inherit packagegroup

PACKAGES = "${PN}-demos"

QTDEMOS ?= "quicky ${COMMERCIAL_QT} fotowall"

RDEPENDS_${PN}-demos = "${QTDEMOS}"
