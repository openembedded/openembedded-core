# Override SELECTED_OPTIMIZATION and BUILD_OPTIMIZATION when fragment 'core/yocto/debug-optimize' is enabled.
DEBUG_OPTIMIZATION ?= "-Og ${DEBUG_LEVELFLAG}"
SELECTED_OPTIMIZATION ?= "${DEBUG_OPTIMIZATION}"
# compiler flags for native/nativesdk
BUILD_OPTIMIZATION ?= "-Og -g"
