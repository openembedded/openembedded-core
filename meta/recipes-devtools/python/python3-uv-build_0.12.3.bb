SUMMARY = "The build backend part of an 'An extremely fast Python package and project manager, written in Rust.'"
HOMEPAGE = "https://pypi.org/project/uv-build/"

LICENSE = "Apache-2.0 AND BSD-2-Clause AND MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=86d3f3a95c324c9479bd8986968f4327 \
                    file://LICENSE-MIT;md5=45674e482567aa99fe883d3270b11184 \
                    file://crates/uv-build/LICENSE-APACHE;md5=86d3f3a95c324c9479bd8986968f4327 \
                    file://crates/uv-build/LICENSE-MIT;md5=45674e482567aa99fe883d3270b11184 \
                    file://crates/uv-pep440/License-Apache;md5=e23fadd6ceef8c618fc1c65191d846fa \
                    file://crates/uv-pep440/License-BSD;md5=ef7a6027dc4c2389b9afad7e690274c7 \
                    file://crates/uv-pep508/License-Apache;md5=e23fadd6ceef8c618fc1c65191d846fa \
                    file://crates/uv-pep508/License-BSD;md5=ef7a6027dc4c2389b9afad7e690274c7"

SRC_URI[sha256sum] = "b6f51ed4411a35f008c9ef6e28016ac8f167f5c14fb2d0e453a9a97246b14206"

require ${BPN}-crates.inc

inherit pypi python_maturin cargo-update-recipe-crates pkgconfig

DEPENDS += "zstd"

PYPI_PACKAGE = "uv_build"

BBCLASSEXTEND = "native"
INSANE_SKIP:${PN} = "already-stripped"
