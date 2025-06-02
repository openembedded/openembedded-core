SUMMARY = "Build and publish crates with pyo3, rust-cpython, cffi bindings and rust binaries as python packages"
HOMEPAGE = "https://github.com/pyo3/maturin"
SECTION = "devel/python"
LICENSE = "MIT | Apache-2.0"
LIC_FILES_CHKSUM = "file://license-apache;md5=1836efb2eb779966696f473ee8540542 \
                    file://license-mit;md5=85fd3b67069cff784d98ebfc7d5c0797"

# This is needed until Cargo.lock has libc-0.2.172+
SRC_URI += "file://0001-Define-more-ioctl-codes-on-riscv32gc-unknown-linux-g.patch;patchdir=${CARGO_VENDORING_DIRECTORY}/libc-0.2.167"
SRC_URI[sha256sum] = "0e0dc2e0bfaa2e1bd238e0236cf8a2b7e2250ccaa29c1aa8d0e61fa664b0289d"

S = "${WORKDIR}/maturin-${PV}"

CFLAGS += "-ffile-prefix-map=${CARGO_HOME}=${TARGET_DBGSRC_DIR}/cargo_home"

DEPENDS += "\
    python3-setuptools-rust-native \
    python3-semantic-version-native \
    python3-setuptools-rust \
"

require ${BPN}-crates.inc

inherit pypi cargo-update-recipe-crates python_pyo3 python_setuptools_build_meta

do_configure() {
    python_pyo3_do_configure
    cargo_common_do_configure
    python_pep517_do_configure
}

RDEPENDS:${PN} += "\
    cargo \
    python3-json \
    rust \
"

RRECOMMENDS:${PN} += "\
    python3-ensurepip \
    python3-pip \
    python3-venv \
"

BBCLASSEXTEND = "native nativesdk"
