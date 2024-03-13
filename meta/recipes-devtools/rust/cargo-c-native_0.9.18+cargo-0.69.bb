SUMMARY = "cargo applet to build and install C-ABI compatible dynamic and static libraries."
HOMEPAGE = "https://crates.io/crates/cargo-c"
LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=384ed0e2e0b2dac094e51fbf93fdcbe0 \
"

SRC_URI = "crate://crates.io/cargo-c/${PV};name=cargo-c"
SRC_URI[cargo-c.sha256sum] = "3089672953dd474200fde35da3db9a7401705d7ed323185878a2923702022da4"
S = "${CARGO_VENDORING_DIRECTORY}/cargo-c-${PV}"

inherit cargo cargo-update-recipe-crates pkgconfig native

DEPENDS = "openssl curl"

require ${BPN}-crates.inc

