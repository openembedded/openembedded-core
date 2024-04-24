SUMMARY = "Templating library for Python"
HOMEPAGE = "http://www.makotemplates.org/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d0995d6f7ba3f186a03118f244e88f57"

PYPI_PACKAGE = "Mako"

inherit pypi python_setuptools_build_meta

SRC_URI[sha256sum] = "e16c01d9ab9c11f7290eef1cfefc093fb5a45ee4a3da09e2fec2e4d1bae54e73"

RDEPENDS:${PN} = "python3-html \
                  python3-markupsafe \
                  python3-netclient \
                  python3-pygments \
                  python3-threading \
"

BBCLASSEXTEND = "native nativesdk"
