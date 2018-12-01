SUMMARY = "C Library for manipulating module metadata files"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=25a3927bff3ee4f5b21bcb0ed3fcd6bb"

SRC_URI = "git://github.com/fedora-modularity/libmodulemd;protocol=https \
           file://0001-spec_tmpl.sh-use-bin-sh-not-usr-bin-sh.patch \
           file://0002-modulemd-v1-meson.build-do-not-generate-gir-or-gtkdo.patch \
           "

PV = "1.7.0"
SRCREV = "9af3e7b4bec2f8daaa857fa668b858e484487710"

S = "${WORKDIR}/git"

inherit meson

EXTRA_OEMESON = "-Ddeveloper_build=false"

DEPENDS += "glib-2.0 libyaml"

BBCLASSEXTEND = "native nativesdk"
