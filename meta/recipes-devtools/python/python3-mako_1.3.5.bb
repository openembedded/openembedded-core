SUMMARY = "Templating library for Python"
HOMEPAGE = "http://www.makotemplates.org/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d0995d6f7ba3f186a03118f244e88f57"

PYPI_PACKAGE = "Mako"

inherit pypi python_setuptools_build_meta

SRC_URI[sha256sum] = "48dbc20568c1d276a2698b36d968fa76161bf127194907ea6fc594fa81f943bc"

RDEPENDS:${PN} = "python3-html \
                  python3-markupsafe \
                  python3-netclient \
                  python3-pygments \
                  python3-threading \
"

BBCLASSEXTEND = "native nativesdk"
