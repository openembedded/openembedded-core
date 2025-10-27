SUMMARY = "Python HTTP library with thread-safe connection pooling, file post support, sanity friendly, and more"
HOMEPAGE = "https://github.com/urllib3/urllib3"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=52d273a3054ced561275d4d15260ecda"

SRC_URI[sha256sum] = "3fc47733c7e419d4bc3f6b3dc2b4f890bb743906a30d56ba4a5bfa4bbff92760"

inherit pypi python_hatchling

DEPENDS += "python3-hatch-vcs-native"

PACKAGECONFIG ??= ""
# This is not recommended for use upstream, and has large dependencies
PACKAGECONFIG[openssl] = ",,,python3-cryptography python3-pyopenssl"

do_install:append() {
    if ! ${@bb.utils.contains("PACKAGECONFIG", "openssl", "true", "false", d)}; then
        rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/urllib3/contrib/pyopenssl.py
    fi
}

RDEPENDS:${PN} += "\
    python3-idna \
    python3-email \
    python3-json \
    python3-netclient \
    python3-threading \
    python3-logging \
"

CVE_PRODUCT = "urllib3"

BBCLASSEXTEND = "native nativesdk"
