#
# Default preferred versions
#

# Force the python versions in one place
PYTHON_BASEVERSION ?= "2.7"
PREFERRED_VERSION_python ?= "2.7.3"
PREFERRED_VERSION_python-native ?= "2.7.3"

# Force the older version of liberation-fonts until we fix the fontforge issue
PREFERRED_VERSION_liberation-fonts ?= "1.04"

# Intel video stack 2013Q3 and newer need a version >=2.99.902
PREFERRED_VERSION_xf86-video-intel ?= "2.99.910"
