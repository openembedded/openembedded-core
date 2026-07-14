SUMMARY = "cargo applet to build and install C-ABI compatible dynamic and static libraries."
HOMEPAGE = "https://crates.io/crates/cargo-c"
LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=384ed0e2e0b2dac094e51fbf93fdcbe0 \
"

SRC_URI = "crate://crates.io/cargo-c/${PV};name=cargo-c \
           file://0001-parking-lot-Use-libc-SYS_futex_time64-on-riscv32.patch;patchdir=../parking_lot_core-0.9.12/ \
"
SRC_URI[cargo-c.sha256sum] = "e033742583695968c690582ab678f65c133779767940c9bc35718e990a235d45"
S = "${CARGO_VENDORING_DIRECTORY}/cargo-c-${PV}"

DEBUG_PREFIX_MAP += "-ffile-prefix-map=${CARGO_HOME}=${TARGET_DBGSRC_DIR}"

inherit cargo cargo-update-recipe-crates pkgconfig

DEPENDS = "openssl curl"

require ${BPN}-crates.inc

BBCLASSEXTEND = "native"
