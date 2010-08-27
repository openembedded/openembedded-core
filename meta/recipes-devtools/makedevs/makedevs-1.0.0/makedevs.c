#define _GNU_SOURCE
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdarg.h>
#include <stdlib.h>
#include <ctype.h>
#include <fcntl.h>
#include <dirent.h>
#include <unistd.h>
#include <time.h>
#include <getopt.h>
#include <libgen.h>
#include <sys/types.h>
#include <sys/stat.h>

#define MINORBITS	8
#define MKDEV(ma,mi)	(((ma) << MINORBITS) | (mi))

/* These are all stolen from busybox's libbb to make
 * error handling simpler (and since I maintain busybox, 
 * I'm rather partial to these for error handling). 
 *  -Erik
 */
static const char *const app_name = "makedevs";
static const char *const memory_exhausted = "memory exhausted";
static char default_rootdir[]=".";
static char *rootdir = default_rootdir;

static void verror_msg(const char *s, va_list p)
{
	fflush(stdout);
	fprintf(stderr, "%s: ", app_name);
	vfprintf(stderr, s, p);
}

static void error_msg_and_die(const char *s, ...)
{
	va_list p;

	va_start(p, s);
	verror_msg(s, p);
	va_end(p);
	putc('\n', stderr);
	exit(EXIT_FAILURE);
}

static void vperror_msg(const char *s, va_list p)
{
	int err = errno;

	if (s == 0)
		s = "";
	verror_msg(s, p);
	if (*s)
		s = ": ";
	fprintf(stderr, "%s%s\n", s, strerror(err));
}

#if 0
static void perror_msg(const char *s, ...)
{
	va_list p;

	va_start(p, s);
	vperror_msg(s, p);
	va_end(p);
}
#endif

static void perror_msg_and_die(const char *s, ...)
{
	va_list p;

	va_start(p, s);
	vperror_msg(s, p);
	va_end(p);
	exit(EXIT_FAILURE);
}

static FILE *xfopen(const char *path, const char *mode)
{
	FILE *fp;

	if ((fp = fopen(path, mode)) == NULL)
		perror_msg_and_die("%s", path);
	return fp;
}

static char *xstrdup(const char *s)
{
	char *t;

	if (s == NULL)
		return NULL;

	t = strdup(s);

	if (t == NULL)
		error_msg_and_die(memory_exhausted);

	return t;
}


static void add_new_directory(char *name, char *path, 
		unsigned long uid, unsigned long gid, unsigned long mode)
{
	mkdir(path, mode);
	chown(path, uid, gid);
//	printf("Directory: %s %s  UID: %ld  GID %ld  MODE: %ld\n", path, name, uid, gid, mode);
}

static void add_new_device(char *name, char *path, unsigned long uid, 
	unsigned long gid, unsigned long mode, dev_t rdev)
{
	int status;
	struct stat sb;
	time_t timestamp = time(NULL);

	memset(&sb, 0, sizeof(struct stat));
	status = lstat(path, &sb);

	if (status >= 0) {
		/* It is ok for some types of files to not exit on disk (such as
		 * device nodes), but if they _do_ exist the specified mode had
		 * better match the actual file or strange things will happen.... */
		if ((mode & S_IFMT) != (sb.st_mode & S_IFMT))
			error_msg_and_die("%s: file type does not match specified type!", path);
		timestamp = sb.st_mtime;
	}

	mknod(name, mode, rdev);
	chown(path, uid, gid);
//	printf("Device: %s %s  UID: %ld  GID: %ld  MODE: %ld  MAJOR: %d  MINOR: %d\n",
//			path, name, uid, gid, mode, (short)(rdev >> 8), (short)(rdev & 0xff));
}

static void add_new_file(char *name, char *path, unsigned long uid,
				  unsigned long gid, unsigned long mode)
{
	int fd = open(path,O_CREAT | O_WRONLY, mode);
	if (fd < 0) { 
		error_msg_and_die("%s: file can not be created!", path);
	} else {
		close(fd);
	} 
	chmod(path, mode);
	chown(path, uid, gid);
//	printf("File: %s %s  UID: %ld  GID: %ld  MODE: %ld\n",
//			path, name, gid, uid, mode);
}


static void add_new_fifo(char *name, char *path, unsigned long uid,
				  unsigned long gid, unsigned long mode)
{
	if (mknod(path, mode, 0))
		error_msg_and_die("%s: file can not be created with mknod!", path);
	chown(path, uid, gid);
//	printf("File: %s %s  UID: %ld  GID: %ld  MODE: %ld\n",
//			path, name, gid, uid, mode);
}


/*  device table entries take the form of:
    <path>	<type> <mode>	<uid>	<gid>	<major>	<minor>	<start>	<inc>	<count>
    /dev/mem    c      640      0       0       1       1       0        0        -

    type can be one of: 
	f	A regular file
	d	Directory
	c	Character special device file
	b	Block special device file
	p	Fifo (named pipe)

    I don't bother with symlinks (permissions are irrelevant), hard
    links (special cases of regular files), or sockets (why bother).

    Regular files must exist in the target root directory.  If a char,
    block, fifo, or directory does not exist, it will be created.
*/
static int interpret_table_entry(char *line)
{
	char *name;
	char path[4096], type;
	unsigned long mode = 0755, uid = 0, gid = 0, major = 0, minor = 0;
	unsigned long start = 0, increment = 1, count = 0;

	if (0 > sscanf(line, "%40s %c %lo %lu %lu %lu %lu %lu %lu %lu", path,
		    &type, &mode, &uid, &gid, &major, &minor, &start,
		    &increment, &count)) 
	{
		return 1;
	}

	if (!strcmp(path, "/")) {
		error_msg_and_die("Device table entries require absolute paths");
	}
	name = xstrdup(path + 1);
	sprintf(path, "%s/%s\0", rootdir, name);

	switch (type) {
	case 'd':
		mode |= S_IFDIR;
		add_new_directory(name, path, uid, gid, mode);
		break;
	case 'f':
		mode |= S_IFREG;
		add_new_file(name, path, uid, gid, mode);
		break;
	case 'p':
		mode |= S_IFIFO;
		add_new_fifo(name, path, uid, gid, mode);
		break;
	case 'c':
	case 'b':
		mode |= (type == 'c') ? S_IFCHR : S_IFBLK;
		if (count > 0) {
			int i;
			dev_t rdev;
			char buf[80];

			for (i = start; i < count; i++) {
				sprintf(buf, "%s%d", name, i);
				/* FIXME:  MKDEV uses illicit insider knowledge of kernel 
				 * major/minor representation...  */
				rdev = MKDEV(major, minor + (i * increment - start));
				add_new_device(buf, path, uid, gid, mode, rdev);
			}
		} else {
			/* FIXME:  MKDEV uses illicit insider knowledge of kernel 
			 * major/minor representation...  */
			dev_t rdev = MKDEV(major, minor);

			add_new_device(name, path, uid, gid, mode, rdev);
		}
		break;
	default:
		error_msg_and_die("Unsupported file type");
	}
	if (name) free(name);
	return 0;
}


static void parse_device_table(FILE * file)
{
	char *line;
	size_t length = 256;
	int len = 0;

	/* Looks ok so far.  The general plan now is to read in one
	 * line at a time, check for leading comment delimiters ('#'),
	 * then try and parse the line as a device table.  If we fail
	 * to parse things, try and help the poor fool to fix their
	 * device table with a useful error msg... */

	if((line = (char *)malloc(length)) == NULL) {
		fclose(file);
		return;
	}

	while ((len = getline(&line, &length, file)) != -1) {
		/* First trim off any whitespace */

		/* trim trailing whitespace */
		while (len > 0 && isspace(line[len - 1]))
			line[--len] = '\0';

		/* trim leading whitespace */
		memmove(line, &line[strspn(line, " \n\r\t\v")], len + 1);

		/* If this is NOT a comment line, try to interpret it */
		if (*line != '#') interpret_table_entry(line);
	}
	if (line) free(line);

	fclose(file);
}

static int go(char *dname, FILE * devtable)
{
	struct stat sb;

	if (lstat(dname, &sb)) {
		perror_msg_and_die("%s", dname);
	}
	if (chdir(dname))
		perror_msg_and_die("%s", dname);

	if (devtable)
		parse_device_table(devtable);

	return 0;
}


static struct option long_options[] = {
	{"root", 1, NULL, 'r'},
	{"help", 0, NULL, 'h'},
	{"squash", 0, NULL, 'q'},
	{"version", 0, NULL, 'v'},
	{"devtable", 1, NULL, 'D'},
	{NULL, 0, NULL, 0}
};

static char *helptext =
	"Usage: makedevs [OPTIONS]\n"
	"Build entries based upon device_table.txt\n\n"
	"Options:\n"
	"  -r, -d, --root=DIR     Build filesystem from directory DIR (default: cwd)\n"
	"  -D, --devtable=FILE    Use the named FILE as a device table file\n"
	"  -q, --squash           Squash permissions and owners making all files be owned by root\n"
	"  -h, --help             Display this help text\n"
	"  -v, --version          Display version information\n\n";


static char *revtext = "$Revision: 0.1 $";

int main(int argc, char **argv)
{
	int c, opt;
	extern char *optarg;
	struct stat statbuf;
	FILE *devtable = NULL;

	umask (0);

	while ((opt = getopt_long(argc, argv, "D:d:r:qhv", 
			long_options, &c)) >= 0) {
		switch (opt) {
		case 'D':
			devtable = xfopen(optarg, "r");
			if (fstat(fileno(devtable), &statbuf) < 0)
				perror_msg_and_die(optarg);
			if (statbuf.st_size < 10)
				error_msg_and_die("%s: not a proper device table file", optarg);
			break;
		case 'h':
			fprintf(stderr, helptext);
			exit(1);
		case 'r':
		case 'd':				/* for compatibility with mkfs.jffs, genext2fs, etc... */
			if (rootdir != default_rootdir) {
				error_msg_and_die("root directory specified more than once");
			}
			rootdir = xstrdup(optarg);
			break;

		case 'v':
			fprintf(stderr, "makedevs revision %.*s\n",
					(int) strlen(revtext) - 13, revtext + 11);
			exit(1);
		}
	}

	go(rootdir, devtable);
	return 0;
}
