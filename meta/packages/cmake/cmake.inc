# Copyright (C) 2005, Koninklijke Philips Electronics NV.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION = "A cross-platform, open-source make system"
HOMEPAGE = "http://www.cmake.org/"
LICENSE = "Berkely-style license"
SECTION = "console/utils"

SRC_URI = "http://www.cmake.org/files/v${CMAKE_MAJOR_VERSION}/cmake-${PV}.tar.gz"

inherit autotools

S = "${WORKDIR}/cmake-${PV}"

do_configure () {
	./configure --prefix=${prefix} || die "./bootstrap failed"
}
