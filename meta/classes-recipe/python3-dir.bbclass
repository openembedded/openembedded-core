#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

PYTHON_BASEVERSION = "3.14"
PYTHON_ABI = ""
PYTHON_MAINVERSION = "${PYTHON_BASEVERSION}${PYTHON_ABI}"
PYTHON_DIR = "python${PYTHON_MAINVERSION}"
PYTHON_PN = "python3"
PYTHON_SITEPACKAGES_DIR = "${libdir}/${PYTHON_DIR}/site-packages"
