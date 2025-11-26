SUMMARY = "cargo applet to build and install C-ABI compatible dynamic and static libraries."
HOMEPAGE = "https://crates.io/crates/cargo-c"
LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=384ed0e2e0b2dac094e51fbf93fdcbe0 \
"

SRC_URI = "crate://crates.io/cargo-c/${PV};name=cargo-c \
           file://0001-parking-lot-Use-libc-SYS_futex_time64-on-riscv32.patch;patchdir=../parking_lot_core-0.9.12/ \
"
SRC_URI[cargo-c.sha256sum] = "2a15984245971462c8e0e6c4e7f1d11f1693af9f7fa11b35c099ab76d749c523"
S = "${CARGO_VENDORING_DIRECTORY}/cargo-c-${PV}"

DEBUG_PREFIX_MAP += "-ffile-prefix-map=${CARGO_HOME}=${TARGET_DBGSRC_DIR}"

inherit cargo cargo-update-recipe-crates pkgconfig

DEPENDS = "openssl curl"

require ${BPN}-crates.inc

BBCLASSEXTEND = "native"
