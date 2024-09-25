SUMMARY = "An implementation of the WebSocket Protocol (RFC 6455)"
HOMEPAGE = "https://github.com/aaugustin/websockets"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=51924a6af4495b8cfaee1b1da869b6f4"

inherit pypi python_setuptools_build_meta

SRC_URI[sha256sum] = "a3b3366087c1bc0a2795111edcadddb8b3b59509d5db5d7ea3fdd69f954a8878"

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} = " \
    python3-asyncio \
"
