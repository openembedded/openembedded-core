# Copyright (C) 2005, Koninklijke Philips Electronics NV.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

SUMMARY = "Cross-platform, open-source make system"
HOMEPAGE = "http://www.cmake.org/"
BUGTRACKER = "http://public.kitware.com/Bug/my_view_page.php"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://Copyright.txt;md5=891b81f415fb25fbad03e41f87fd5dfb \
                    file://Source/cmake.h;md5=4494dee184212fc89c469c3acd555a14;beginline=1;endline=3 \
                    "

CMAKE_MAJOR_VERSION = "${@'.'.join(d.getVar('PV').split('.')[0:2])}"

SRC_URI = "https://cmake.org/files/v${CMAKE_MAJOR_VERSION}/cmake-${PV}.tar.gz \
           file://0001-cmake-Prevent-the-detection-of-Qt5.patch \
           file://0002-cmake-support-OpenEmbedded-Qt4-tool-binary-names.patch \
           file://0003-Fail-silently-if-system-Qt-installation-is-broken.patch \
"

SRC_URI[md5sum] = "72e168b3bad2f9c34dcebbad7af56ff0"
SRC_URI[sha256sum] = "8f864e9f78917de3e1483e256270daabc4a321741592c5b36af028e72bff87f5"

UPSTREAM_CHECK_REGEX = "cmake-(?P<pver>\d+(\.\d+)+)\.tar"
