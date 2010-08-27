
# WARNING: This file is AUTO GENERATED: Manual edits will be lost next time I regenerate the file.
# Generator: '../../contrib/python/generate-manifest-2.6.py' Version 20091004 (C) 2002-2009 Michael 'Mickey' Lauer <mlauer@vanille-media.de>
# Visit the Python for Embedded Systems Site => http://www.Vanille.de/projects/python.spy

 

PROVIDES+="python-profile python-threading python-distutils python-doctest python-stringold python-codecs python-ctypes python-pickle python-datetime python-core python-io python-compiler python-compression python-re python-xmlrpc python-terminal python-email python-image python-tests python-core-dbg python-resource python-json python-devel python-difflib python-math python-syslog python-hotshot python-unixadmin python-textutils python-tkinter python-gdbm python-elementtree python-fcntl python-netclient python-pprint python-netserver python-curses python-smtpd python-html python-readline python-subprocess python-pydoc python-logging python-mailbox python-xml python-mime python-sqlite3 python-sqlite3-tests python-unittest python-multiprocessing python-robotparser python-compile python-debugger python-pkgutil python-shell python-bsddb python-mmap python-zlib python-db python-crypt python-idle python-lang python-audio "

PACKAGES="python-core-dbg python-profile python-threading python-distutils python-doctest python-stringold python-codecs python-ctypes python-pickle python-datetime python-core python-io python-compiler python-compression python-re python-xmlrpc python-terminal python-email python-image python-tests python-resource python-json python-devel python-difflib python-math python-syslog python-hotshot python-unixadmin python-textutils python-tkinter python-gdbm python-elementtree python-fcntl python-netclient python-pprint python-netserver python-curses python-smtpd python-html python-readline python-subprocess python-pydoc python-logging python-mailbox python-xml python-mime python-sqlite3 python-sqlite3-tests python-unittest python-multiprocessing python-robotparser python-compile python-debugger python-pkgutil python-shell python-bsddb python-mmap python-zlib python-db python-crypt python-idle python-lang python-audio python-modules"

DESCRIPTION_python-profile="Python Basic Profiling Support"
RDEPENDS_python-profile="python-core python-textutils"
FILES_python-profile="${libdir}/python2.6/profile.* ${libdir}/python2.6/pstats.* ${libdir}/python2.6/cProfile.* ${libdir}/python2.6/lib-dynload/_lsprof.so "

DESCRIPTION_python-threading="Python Threading & Synchronization Support"
RDEPENDS_python-threading="python-core python-lang"
FILES_python-threading="${libdir}/python2.6/_threading_local.* ${libdir}/python2.6/dummy_thread.* ${libdir}/python2.6/dummy_threading.* ${libdir}/python2.6/mutex.* ${libdir}/python2.6/threading.* ${libdir}/python2.6/Queue.* "

DESCRIPTION_python-distutils="Python Distribution Utilities"
RDEPENDS_python-distutils="python-core"
FILES_python-distutils="${libdir}/python2.6/config ${libdir}/python2.6/distutils "

DESCRIPTION_python-doctest="Python framework for running examples in docstrings."
RDEPENDS_python-doctest="python-core python-lang python-io python-re python-unittest python-debugger python-difflib"
FILES_python-doctest="${libdir}/python2.6/doctest.* "

DESCRIPTION_python-stringold="Python String APIs [deprecated]"
RDEPENDS_python-stringold="python-core python-re"
FILES_python-stringold="${libdir}/python2.6/lib-dynload/strop.so ${libdir}/python2.6/string.* "

DESCRIPTION_python-codecs="Python Codecs, Encodings & i18n Support"
RDEPENDS_python-codecs="python-core python-lang"
FILES_python-codecs="${libdir}/python2.6/codecs.* ${libdir}/python2.6/encodings ${libdir}/python2.6/gettext.* ${libdir}/python2.6/locale.* ${libdir}/python2.6/lib-dynload/_locale.so ${libdir}/python2.6/lib-dynload/unicodedata.so ${libdir}/python2.6/stringprep.* ${libdir}/python2.6/xdrlib.* "

DESCRIPTION_python-ctypes="Python C Types Support"
RDEPENDS_python-ctypes="python-core"
FILES_python-ctypes="${libdir}/python2.6/ctypes ${libdir}/python2.6/lib-dynload/_ctypes.so "

DESCRIPTION_python-pickle="Python Persistence Support"
RDEPENDS_python-pickle="python-core python-codecs python-io python-re"
FILES_python-pickle="${libdir}/python2.6/pickle.* ${libdir}/python2.6/shelve.* ${libdir}/python2.6/lib-dynload/cPickle.so "

DESCRIPTION_python-datetime="Python Calendar and Time support"
RDEPENDS_python-datetime="python-core python-codecs"
FILES_python-datetime="${libdir}/python2.6/_strptime.* ${libdir}/python2.6/calendar.* ${libdir}/python2.6/lib-dynload/datetime.so "

DESCRIPTION_python-core="Python Interpreter and core modules (needed!)"
RDEPENDS_python-core=""
FILES_python-core="${libdir}/python2.6/__future__.* ${libdir}/python2.6/_abcoll.* ${libdir}/python2.6/abc.* ${libdir}/python2.6/copy.* ${libdir}/python2.6/copy_reg.* ${libdir}/python2.6/ConfigParser.* ${libdir}/python2.6/genericpath.* ${libdir}/python2.6/getopt.* ${libdir}/python2.6/linecache.* ${libdir}/python2.6/new.* ${libdir}/python2.6/os.* ${libdir}/python2.6/posixpath.* ${libdir}/python2.6/struct.* ${libdir}/python2.6/warnings.* ${libdir}/python2.6/site.* ${libdir}/python2.6/stat.* ${libdir}/python2.6/UserDict.* ${libdir}/python2.6/UserList.* ${libdir}/python2.6/UserString.* ${libdir}/python2.6/lib-dynload/binascii.so ${libdir}/python2.6/lib-dynload/_struct.so ${libdir}/python2.6/lib-dynload/time.so ${libdir}/python2.6/lib-dynload/xreadlines.so ${libdir}/python2.6/types.* ${bindir}/python* "

DESCRIPTION_python-io="Python Low-Level I/O"
RDEPENDS_python-io="python-core python-math"
FILES_python-io="${libdir}/python2.6/lib-dynload/_socket.so ${libdir}/python2.6/lib-dynload/_ssl.so ${libdir}/python2.6/lib-dynload/select.so ${libdir}/python2.6/lib-dynload/termios.so ${libdir}/python2.6/lib-dynload/cStringIO.so ${libdir}/python2.6/pipes.* ${libdir}/python2.6/socket.* ${libdir}/python2.6/tempfile.* ${libdir}/python2.6/StringIO.* "

DESCRIPTION_python-compiler="Python Compiler Support"
RDEPENDS_python-compiler="python-core"
FILES_python-compiler="${libdir}/python2.6/compiler "

DESCRIPTION_python-compression="Python High Level Compression Support"
RDEPENDS_python-compression="python-core python-zlib"
FILES_python-compression="${libdir}/python2.6/gzip.* ${libdir}/python2.6/zipfile.* ${libdir}/python2.6/tarfile.* ${libdir}/python2.6/lib-dynload/bz2.so "

DESCRIPTION_python-re="Python Regular Expression APIs"
RDEPENDS_python-re="python-core"
FILES_python-re="${libdir}/python2.6/re.* ${libdir}/python2.6/sre.* ${libdir}/python2.6/sre_compile.* ${libdir}/python2.6/sre_constants* ${libdir}/python2.6/sre_parse.* "

DESCRIPTION_python-xmlrpc="Python XMLRPC Support"
RDEPENDS_python-xmlrpc="python-core python-xml python-netserver python-lang"
FILES_python-xmlrpc="${libdir}/python2.6/xmlrpclib.* ${libdir}/python2.6/SimpleXMLRPCServer.* "

DESCRIPTION_python-terminal="Python Terminal Controlling Support"
RDEPENDS_python-terminal="python-core python-io"
FILES_python-terminal="${libdir}/python2.6/pty.* ${libdir}/python2.6/tty.* "

DESCRIPTION_python-email="Python Email Support"
RDEPENDS_python-email="python-core python-io python-re python-mime python-audio python-image python-netclient"
FILES_python-email="${libdir}/python2.6/imaplib.* ${libdir}/python2.6/email "

DESCRIPTION_python-image="Python Graphical Image Handling"
RDEPENDS_python-image="python-core"
FILES_python-image="${libdir}/python2.6/colorsys.* ${libdir}/python2.6/imghdr.* ${libdir}/python2.6/lib-dynload/imageop.so ${libdir}/python2.6/lib-dynload/rgbimg.so "

DESCRIPTION_python-tests="Python Tests"
RDEPENDS_python-tests="python-core"
FILES_python-tests="${libdir}/python2.6/test "

DESCRIPTION_python-core-dbg="Python core module debug information"
RDEPENDS_python-core-dbg="python-core"
FILES_python-core-dbg="${libdir}/python2.6/config/.debug ${libdir}/python2.6/lib-dynload/.debug ${bindir}/.debug ${libdir}/.debug "

DESCRIPTION_python-resource="Python Resource Control Interface"
RDEPENDS_python-resource="python-core"
FILES_python-resource="${libdir}/python2.6/lib-dynload/resource.so "

DESCRIPTION_python-json="Python JSON Support"
RDEPENDS_python-json="python-core python-math python-re"
FILES_python-json="${libdir}/python2.6/json "

DESCRIPTION_python-devel="Python Development Package"
RDEPENDS_python-devel="python-core"
FILES_python-devel="${includedir} ${libdir}/libpython2.6.so ${libdir}/python2.6/config "

DESCRIPTION_python-difflib="Python helpers for computing deltas between objects."
RDEPENDS_python-difflib="python-lang python-re"
FILES_python-difflib="${libdir}/python2.6/difflib.* "

DESCRIPTION_python-math="Python Math Support"
RDEPENDS_python-math="python-core"
FILES_python-math="${libdir}/python2.6/lib-dynload/cmath.so ${libdir}/python2.6/lib-dynload/math.so ${libdir}/python2.6/lib-dynload/_random.so ${libdir}/python2.6/random.* ${libdir}/python2.6/sets.* "

DESCRIPTION_python-syslog="Python Syslog Interface"
RDEPENDS_python-syslog="python-core"
FILES_python-syslog="${libdir}/python2.6/lib-dynload/syslog.so "

DESCRIPTION_python-hotshot="Python Hotshot Profiler"
RDEPENDS_python-hotshot="python-core"
FILES_python-hotshot="${libdir}/python2.6/hotshot ${libdir}/python2.6/lib-dynload/_hotshot.so "

DESCRIPTION_python-unixadmin="Python Unix Administration Support"
RDEPENDS_python-unixadmin="python-core"
FILES_python-unixadmin="${libdir}/python2.6/lib-dynload/nis.so ${libdir}/python2.6/lib-dynload/grp.so ${libdir}/python2.6/lib-dynload/pwd.so ${libdir}/python2.6/getpass.* "

DESCRIPTION_python-textutils="Python Option Parsing, Text Wrapping and Comma-Separated-Value Support"
RDEPENDS_python-textutils="python-core python-io python-re python-stringold"
FILES_python-textutils="${libdir}/python2.6/lib-dynload/_csv.so ${libdir}/python2.6/csv.* ${libdir}/python2.6/optparse.* ${libdir}/python2.6/textwrap.* "

DESCRIPTION_python-tkinter="Python Tcl/Tk Bindings"
RDEPENDS_python-tkinter="python-core"
FILES_python-tkinter="${libdir}/python2.6/lib-dynload/_tkinter.so ${libdir}/python2.6/lib-tk "

DESCRIPTION_python-gdbm="Python GNU Database Support"
RDEPENDS_python-gdbm="python-core"
FILES_python-gdbm="${libdir}/python2.6/lib-dynload/gdbm.so "

DESCRIPTION_python-elementtree="Python elementree"
RDEPENDS_python-elementtree="python-core"
FILES_python-elementtree="${libdir}/python2.6/lib-dynload/_elementtree.so "

DESCRIPTION_python-fcntl="Python's fcntl Interface"
RDEPENDS_python-fcntl="python-core"
FILES_python-fcntl="${libdir}/python2.6/lib-dynload/fcntl.so "

DESCRIPTION_python-netclient="Python Internet Protocol Clients"
RDEPENDS_python-netclient="python-core python-crypt python-datetime python-io python-lang python-logging python-mime"
FILES_python-netclient="${libdir}/python2.6/*Cookie*.* ${libdir}/python2.6/base64.* ${libdir}/python2.6/cookielib.* ${libdir}/python2.6/ftplib.* ${libdir}/python2.6/gopherlib.* ${libdir}/python2.6/hmac.* ${libdir}/python2.6/httplib.* ${libdir}/python2.6/mimetypes.* ${libdir}/python2.6/nntplib.* ${libdir}/python2.6/poplib.* ${libdir}/python2.6/smtplib.* ${libdir}/python2.6/telnetlib.* ${libdir}/python2.6/urllib.* ${libdir}/python2.6/urllib2.* ${libdir}/python2.6/urlparse.* ${libdir}/python2.6/uuid.* ${libdir}/python2.6/rfc822.* ${libdir}/python2.6/mimetools.* "

DESCRIPTION_python-pprint="Python Pretty-Print Support"
RDEPENDS_python-pprint="python-core"
FILES_python-pprint="${libdir}/python2.6/pprint.* "

DESCRIPTION_python-netserver="Python Internet Protocol Servers"
RDEPENDS_python-netserver="python-core python-netclient"
FILES_python-netserver="${libdir}/python2.6/cgi.* ${libdir}/python2.6/*HTTPServer.* ${libdir}/python2.6/SocketServer.* "

DESCRIPTION_python-curses="Python Curses Support"
RDEPENDS_python-curses="python-core"
FILES_python-curses="${libdir}/python2.6/curses ${libdir}/python2.6/lib-dynload/_curses.so ${libdir}/python2.6/lib-dynload/_curses_panel.so "

DESCRIPTION_python-smtpd="Python Simple Mail Transport Daemon"
RDEPENDS_python-smtpd="python-core python-netserver python-email python-mime"
FILES_python-smtpd="${bindir}/smtpd.* "

DESCRIPTION_python-html="Python HTML Processing"
RDEPENDS_python-html="python-core"
FILES_python-html="${libdir}/python2.6/formatter.* ${libdir}/python2.6/htmlentitydefs.* ${libdir}/python2.6/htmllib.* ${libdir}/python2.6/markupbase.* ${libdir}/python2.6/sgmllib.* "

DESCRIPTION_python-readline="Python Readline Support"
RDEPENDS_python-readline="python-core"
FILES_python-readline="${libdir}/python2.6/lib-dynload/readline.so ${libdir}/python2.6/rlcompleter.* "

DESCRIPTION_python-subprocess="Python Subprocess Support"
RDEPENDS_python-subprocess="python-core python-io python-re python-fcntl python-pickle"
FILES_python-subprocess="${libdir}/python2.6/subprocess.* "

DESCRIPTION_python-pydoc="Python Interactive Help Support"
RDEPENDS_python-pydoc="python-core python-lang python-stringold python-re"
FILES_python-pydoc="${bindir}/pydoc ${libdir}/python2.6/pydoc.* "

DESCRIPTION_python-logging="Python Logging Support"
RDEPENDS_python-logging="python-core python-io python-lang python-pickle python-stringold"
FILES_python-logging="${libdir}/python2.6/logging "

DESCRIPTION_python-mailbox="Python Mailbox Format Support"
RDEPENDS_python-mailbox="python-core python-mime"
FILES_python-mailbox="${libdir}/python2.6/mailbox.* "

DESCRIPTION_python-xml="Python basic XML support."
RDEPENDS_python-xml="python-core python-re"
FILES_python-xml="${libdir}/python2.6/lib-dynload/pyexpat.so ${libdir}/python2.6/xml ${libdir}/python2.6/xmllib.* "

DESCRIPTION_python-mime="Python MIME Handling APIs"
RDEPENDS_python-mime="python-core python-io"
FILES_python-mime="${libdir}/python2.6/mimetools.* ${libdir}/python2.6/uu.* ${libdir}/python2.6/quopri.* ${libdir}/python2.6/rfc822.* "

DESCRIPTION_python-sqlite3="Python Sqlite3 Database Support"
RDEPENDS_python-sqlite3="python-core python-datetime python-lang python-crypt python-io python-threading python-zlib"
FILES_python-sqlite3="${libdir}/python2.6/lib-dynload/_sqlite3.so ${libdir}/python2.6/sqlite3/dbapi2.* ${libdir}/python2.6/sqlite3/__init__.* ${libdir}/python2.6/sqlite3/dump.* "

DESCRIPTION_python-sqlite3-tests="Python Sqlite3 Database Support Tests"
RDEPENDS_python-sqlite3-tests="python-core python-sqlite3"
FILES_python-sqlite3-tests="${libdir}/python2.6/sqlite3/test "

DESCRIPTION_python-unittest="Python Unit Testing Framework"
RDEPENDS_python-unittest="python-core python-stringold python-lang"
FILES_python-unittest="${libdir}/python2.6/unittest.* "

DESCRIPTION_python-multiprocessing="Python Multiprocessing Support"
RDEPENDS_python-multiprocessing="python-core python-io python-lang"
FILES_python-multiprocessing="${libdir}/python2.6/lib-dynload/_multiprocessing.so ${libdir}/python2.6/multiprocessing "

DESCRIPTION_python-robotparser="Python robots.txt parser"
RDEPENDS_python-robotparser="python-core python-netclient"
FILES_python-robotparser="${libdir}/python2.6/robotparser.* "

DESCRIPTION_python-compile="Python Bytecode Compilation Support"
RDEPENDS_python-compile="python-core"
FILES_python-compile="${libdir}/python2.6/py_compile.* ${libdir}/python2.6/compileall.* "

DESCRIPTION_python-debugger="Python Debugger"
RDEPENDS_python-debugger="python-core python-io python-lang python-re python-stringold python-shell python-pprint"
FILES_python-debugger="${libdir}/python2.6/bdb.* ${libdir}/python2.6/pdb.* "

DESCRIPTION_python-pkgutil="Python Package Extension Utility Support"
RDEPENDS_python-pkgutil="python-core"
FILES_python-pkgutil="${libdir}/python2.6/pkgutil.* "

DESCRIPTION_python-shell="Python Shell-Like Functionality"
RDEPENDS_python-shell="python-core python-re"
FILES_python-shell="${libdir}/python2.6/cmd.* ${libdir}/python2.6/commands.* ${libdir}/python2.6/dircache.* ${libdir}/python2.6/fnmatch.* ${libdir}/python2.6/glob.* ${libdir}/python2.6/popen2.* ${libdir}/python2.6/shlex.* ${libdir}/python2.6/shutil.* "

DESCRIPTION_python-bsddb="Python Berkeley Database Bindings"
RDEPENDS_python-bsddb="python-core"
FILES_python-bsddb="${libdir}/python2.6/bsddb ${libdir}/python2.6/lib-dynload/_bsddb.so "

DESCRIPTION_python-mmap="Python Memory-Mapped-File Support"
RDEPENDS_python-mmap="python-core python-io"
FILES_python-mmap="${libdir}/python2.6/lib-dynload/mmap.so "

DESCRIPTION_python-zlib="Python zlib Support."
RDEPENDS_python-zlib="python-core"
FILES_python-zlib="${libdir}/python2.6/lib-dynload/zlib.so "

DESCRIPTION_python-db="Python File-Based Database Support"
RDEPENDS_python-db="python-core"
FILES_python-db="${libdir}/python2.6/anydbm.* ${libdir}/python2.6/dumbdbm.* ${libdir}/python2.6/whichdb.* "

DESCRIPTION_python-crypt="Python Basic Cryptographic and Hashing Support"
RDEPENDS_python-crypt="python-core"
FILES_python-crypt="${libdir}/python2.6/hashlib.* ${libdir}/python2.6/md5.* ${libdir}/python2.6/sha.* ${libdir}/python2.6/lib-dynload/crypt.so ${libdir}/python2.6/lib-dynload/_hashlib.so ${libdir}/python2.6/lib-dynload/_sha256.so ${libdir}/python2.6/lib-dynload/_sha512.so "

DESCRIPTION_python-idle="Python Integrated Development Environment"
RDEPENDS_python-idle="python-core python-tkinter"
FILES_python-idle="${bindir}/idle ${libdir}/python2.6/idlelib "

DESCRIPTION_python-lang="Python Low-Level Language Support"
RDEPENDS_python-lang="python-core"
FILES_python-lang="${libdir}/python2.6/lib-dynload/_bisect.so ${libdir}/python2.6/lib-dynload/_collections.so ${libdir}/python2.6/lib-dynload/_heapq.so ${libdir}/python2.6/lib-dynload/_weakref.so ${libdir}/python2.6/lib-dynload/_functools.so ${libdir}/python2.6/lib-dynload/array.so ${libdir}/python2.6/lib-dynload/itertools.so ${libdir}/python2.6/lib-dynload/operator.so ${libdir}/python2.6/lib-dynload/parser.so ${libdir}/python2.6/atexit.* ${libdir}/python2.6/bisect.* ${libdir}/python2.6/code.* ${libdir}/python2.6/codeop.* ${libdir}/python2.6/collections.* ${libdir}/python2.6/dis.* ${libdir}/python2.6/functools.* ${libdir}/python2.6/heapq.* ${libdir}/python2.6/inspect.* ${libdir}/python2.6/keyword.* ${libdir}/python2.6/opcode.* ${libdir}/python2.6/symbol.* ${libdir}/python2.6/repr.* ${libdir}/python2.6/token.* ${libdir}/python2.6/tokenize.* ${libdir}/python2.6/traceback.* ${libdir}/python2.6/linecache.* ${libdir}/python2.6/weakref.* "

DESCRIPTION_python-audio="Python Audio Handling"
RDEPENDS_python-audio="python-core"
FILES_python-audio="${libdir}/python2.6/wave.* ${libdir}/python2.6/chunk.* ${libdir}/python2.6/sndhdr.* ${libdir}/python2.6/lib-dynload/ossaudiodev.so ${libdir}/python2.6/lib-dynload/audioop.so "

DESCRIPTION_python-modules="All Python modules"
RDEPENDS_python-modules="python-profile python-threading python-distutils python-doctest python-stringold python-codecs python-ctypes python-pickle python-datetime python-core python-io python-compiler python-compression python-re python-xmlrpc python-terminal python-email python-image python-tests python-resource python-json python-devel python-difflib python-math python-syslog python-hotshot python-unixadmin python-textutils python-tkinter python-gdbm python-elementtree python-fcntl python-netclient python-pprint python-netserver python-curses python-smtpd python-html python-readline python-subprocess python-pydoc python-logging python-mailbox python-xml python-mime python-sqlite3 python-sqlite3-tests python-unittest python-multiprocessing python-robotparser python-compile python-debugger python-pkgutil python-shell python-bsddb python-mmap python-zlib python-db python-crypt python-idle python-lang python-audio  "
ALLOW_EMPTY_python-modules = "1"


