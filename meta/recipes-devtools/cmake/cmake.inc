# Copyright (C) 2005, Koninklijke Philips Electronics NV.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

SUMMARY = "Cross-platform, open-source make system"
HOMEPAGE = "http://www.cmake.org/"
BUGTRACKER = "http://public.kitware.com/Bug/my_view_page.php"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://Copyright.txt;md5=79b5b78197c74d5c5f9c7ccadeee4e8c \
                    file://Source/cmake.h;md5=4494dee184212fc89c469c3acd555a14;beginline=1;endline=3 \
                    "

CMAKE_MAJOR_VERSION = "${@'.'.join(d.getVar('PV').split('.')[0:2])}"

SRC_URI = "https://cmake.org/files/v${CMAKE_MAJOR_VERSION}/cmake-${PV}.tar.gz \
           file://support-oe-qt4-tools-names.patch \
           file://qt4-fail-silent.patch \
           file://cmake-Prevent-the-detection-of-Qt5.patch \
           "

SRC_URI[md5sum] = "9a726e5ec69618b172aa4b06d18c3998"
SRC_URI[sha256sum] = "7be36ee24b0f5928251b644d29f5ff268330a916944ef4a75e23ba01e7573284"

UPSTREAM_CHECK_REGEX = "cmake-(?P<pver>\d+(\.\d+)+)\.tar"
