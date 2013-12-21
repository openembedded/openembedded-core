# Copyright (C) 2013 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Library of utility functions from BSD systems"
DESCRIPTION = "This library provides useful functions commonly found on BSD systems, \
               and lacking on others like GNU systems, thus making it easier to port \
               projects with strong BSD origins, without needing to embed the same \
               code over and over again on each project."

HOMEPAGE = "http://libbsd.freedesktop.org/wiki/"
LICENSE = "BSD-4-Clause & MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=98a015f07e71239b058398054f506f07"
SECTION = "libs"
DEPENDS = ""

SRC_URI = "http://libbsd.freedesktop.org/releases/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "f6c75f0a9818e323a589bcbd560a0eb4"
SRC_URI[sha256sum] = "9e8f34ffa9c8579c87965a55a82d8ac37a1dc64858f717b7c49452ade277cc62"

inherit autotools pkgconfig
