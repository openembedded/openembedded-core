SUMMARY = "An implementation of the WebSocket Protocol (RFC 6455)"
HOMEPAGE = "https://github.com/aaugustin/websockets"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=51924a6af4495b8cfaee1b1da869b6f4"

inherit pypi python_setuptools_build_meta ptest-python-pytest

SRC_URI += "file://run-ptest"

SRC_URI[sha256sum] = "db234eda965dcce15df96bb9709f587cd87d4d52aaf0e80e2f34ec04c7670c57"

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} = " \
    python3-asyncio \
"

do_install_ptest:append() {
    # These modules import test helpers from tests/asyncio, tests/sync or
    # tests/extensions, which exist in the git checkout but are not part
    # of the sdist, so they fail to collect. Drop them and keep only the
    # self-contained sdist tests.
    for f in test_auth.py test_cli.py test_client.py test_connection.py \
             test_exceptions.py test_frames.py test_http.py test_http11.py \
             test_protocol.py test_server.py test_streams.py; do
        rm -f ${D}${PTEST_PATH}/tests/$f
    done
}
