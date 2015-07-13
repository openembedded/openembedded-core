# The following logging mechanisms are to be used in bash functions of recipes.
# They are intended to map one to one in intention and output format with the
# python recipe logging functions of a similar naming convention: bb.plain(),
# bb.note(), etc.

LOGFIFO = "${T}/fifo.${@os.getpid()}"

# Print the output exactly as it is passed in. Typically used for output of
# tasks that should be seen on the console. Use sparingly.
# Output: logs console
bbplain() {
	printf "%b\0" "bbplain $*" > ${LOGFIFO}
}

# Notify the user of a noteworthy condition. 
# Output: logs
bbnote() {
	printf "%b\0" "bbnote $*" > ${LOGFIFO}
}

# Print a warning to the log. Warnings are non-fatal, and do not
# indicate a build failure.
# Output: logs console
bbwarn() {
	printf "%b\0" "bbwarn $*" > ${LOGFIFO}
}

# Print an error to the log. Errors are non-fatal in that the build can
# continue, but they do indicate a build failure.
# Output: logs console
bberror() {
	printf "%b\0" "bberror $*" > ${LOGFIFO}
}

# Print a fatal error to the log. Fatal errors indicate build failure
# and halt the build, exiting with an error code.
# Output: logs console
bbfatal() {
	printf "%b\0" "bbfatal $*" > ${LOGFIFO}
	exit 1
}

# Like bbfatal, except prevents the suppression of the error log by
# bitbake's UI.
# Output: logs console
bbfatal_log() {
	printf "%b\0" "bbfatal_log $*" > ${LOGFIFO}
	exit 1
}

# Print debug messages. These are appropriate for progress checkpoint
# messages to the logs. Depending on the debug log level, they may also
# go to the console.
# Output: logs console
# Usage: bbdebug 1 "first level debug message"
#        bbdebug 2 "second level debug message"
bbdebug() {
	USAGE='Usage: bbdebug [123] "message"'
	if [ $# -lt 2 ]; then
		bbfatal "$USAGE"
	fi
	
	# Strip off the debug level and ensure it is an integer
	DBGLVL=$1; shift
	NONDIGITS=$(echo "$DBGLVL" | tr -d [:digit:])
	if [ "$NONDIGITS" ]; then
		bbfatal "$USAGE"
	fi

	# All debug output is printed to the logs
	printf "%b\0" "bbdebug $DBGLVL $*" > ${LOGFIFO}
}

