SUMMARY = "Utility library for gitignore style pattern matching of file paths."
HOMEPAGE = "https://github.com/cpburnz/python-path-specification"
SECTION = "devel/python"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=815ca599c9df247a0c7f619bab123dad"

SRC_URI[sha256sum] = "bac5cf97ae2c2876e2d25ebb15078eb04d76e4b98921ee31c6f85ade8b59444d"

inherit pypi python_flit_core

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += "python3-profile"
