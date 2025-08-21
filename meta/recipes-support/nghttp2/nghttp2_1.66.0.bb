SUMMARY = "HTTP/2 C Library and tools"
HOMEPAGE = "https://nghttp2.org/"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=764abdf30b2eadd37ce47dcbce0ea1ec"

SRC_URI = "${GITHUB_BASE_URI}/download/v${PV}/nghttp2-${PV}.tar.xz"
SRC_URI[sha256sum] = "00ba1bdf0ba2c74b2a4fe6c8b1069dc9d82f82608af24442d430df97c6f9e631"

inherit cmake manpages python3native github-releases

PACKAGECONFIG[manpages] = "-DENABLE_DOC=ON,-DENABLE_DOC=OFF"

EXTRA_OECMAKE = "-DENABLE_LIB_ONLY=ON -DENABLE_PYTHON_BINDINGS=OFF"

BBCLASSEXTEND = "native nativesdk"
