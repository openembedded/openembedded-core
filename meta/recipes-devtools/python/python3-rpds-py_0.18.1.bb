SUMMARY = "Python bindings to the Rust rpds crate for persistent data structures."
HOMEPAGE = "https://pypi.org/project/rpds-py/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7767fa537c4596c54141f32882c4a984"

SRC_URI[sha256sum] = "dc48b479d540770c811fbd1eb9ba2bb66951863e448efec2e2c102625328e92f"

require ${BPN}-crates.inc

inherit pypi cargo-update-recipe-crates python_maturin

PYPI_PACKAGE = "rpds_py"

BBCLASSEXTEND = "native nativesdk"
