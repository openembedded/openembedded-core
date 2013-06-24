# Copyright (C) 2013 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

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
SRC_URI[md5sum] = "be8b2e0dc4614699834c49693574fd1a"
SRC_URI[sha256sum] = "5340cf67555a8d92e7652d96540a47986a26eeafb9a0a3e22d3b3e5701ebe23f"

inherit autotools pkgconfig
