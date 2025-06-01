SUMMARY = "Python HTTP library with thread-safe connection pooling, file post support, sanity friendly, and more"
HOMEPAGE = "https://github.com/urllib3/urllib3"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=52d273a3054ced561275d4d15260ecda"

SRC_URI[sha256sum] = "414bc6535b787febd7567804cc015fee39daab8ad86268f1310a9250697de466"

inherit pypi python_hatchling

DEPENDS += " \
    python3-hatch-vcs-native \
"

RDEPENDS:${PN} += "\
    python3-certifi \
    python3-cryptography \
    python3-email \
    python3-idna \
    python3-json \
    python3-netclient \
    python3-pyopenssl \
    python3-threading \
    python3-logging \
"

CVE_PRODUCT = "urllib3"

BBCLASSEXTEND = "native nativesdk"
