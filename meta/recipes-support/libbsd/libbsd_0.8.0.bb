# Copyright (C) 2013 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Library of utility functions from BSD systems"
DESCRIPTION = "This library provides useful functions commonly found on BSD systems, \
               and lacking on others like GNU systems, thus making it easier to port \
               projects with strong BSD origins, without needing to embed the same \
               code over and over again on each project."

HOMEPAGE = "http://libbsd.freedesktop.org/wiki/"
LICENSE = "BSD-4-Clause & MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=b99a70657f41053dc4b49d0ae73f7a78"
SECTION = "libs"
DEPENDS = ""

SRC_URI = " \
	http://libbsd.freedesktop.org/releases/${BPN}-${PV}.tar.xz \
	file://0001-Use-local-SHA512-header.patch \
	"

SRC_URI[md5sum] = "262bdd1aa3bee6066a8c9cb49bb6c584"
SRC_URI[sha256sum] = "fbb732084bd960e4c78b688aac875be98e290cc6fe462b2ff8ee946a6473e38c"

inherit autotools pkgconfig
