//Based on code from
//http://docs.oracle.com/javase/tutorial/essential/io/examples/Find.java
//which supports this article
//http://docs.oracle.com/javase/tutorial/essential/io/find.html
//http://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob
//
//Renamed Find to FileFile and modified to return file list.
//
/*
 * Copyright (c) 2009, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import static java.nio.file.FileVisitResult.*;
import static java.nio.file.FileVisitOption.*;

import java.util.*;

/**
 * Sample code that finds files that
 * match the specified glob pattern.
 * For more information on what
 * constitutes a glob pattern, see
 * http://docs.oracle.com/javase/javatutorials/tutorial/essential/io/fileOps.html#glob
 *
 * The file or directories that match
 * the pattern are printed to
 * standard out.  The number of
 * matches is also printed.
 *
 * When executing this application,
 * you must put the glob pattern
 * in quotes, so the shell will not
 * expand any wild cards:
 *     java Find . -name "*.java"
 */

public class FileFinder 
{

    /**
     * A {@code FileVisitor} that finds
     * all files that match the
     * specified pattern.
     */
    public static class FileFindVisitor
        extends SimpleFileVisitor<Path> 
    {
    	public List<String> m_lstFiles = new ArrayList<String>();
    	
        private final PathMatcher matcher;
        private int numMatches = 0;

        FileFindVisitor(String pattern) 
        {
            matcher = FileSystems.getDefault()
                    .getPathMatcher("glob:" + pattern);
        }

        // Compares the glob pattern against
        // the file or directory name.
        void find(Path file) 
        {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) 
            {
            	
                numMatches++;
                System.out.println(file);
                String sFilePath = file.toString() ;
                m_lstFiles.add(sFilePath) ;
            }
        }

        // Prints the total number of
        // matches to standard out.
        void done() 
        {
            //System.out.println("Matched: " + numMatches);
        	String sSum = "Matched: " + numMatches ;
        }

        // Invoke the pattern matching
        // method on each file.
        @Override
        public FileVisitResult visitFile(Path file,
                BasicFileAttributes attrs) {
            find(file);
            return CONTINUE;
        }

        // Invoke the pattern matching
        // method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir,
                BasicFileAttributes attrs) {
            find(dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file,
                IOException exc) {
            System.err.println(exc);
            return CONTINUE;
        }
    }

    static void usage() {
        System.err.println("java Find <path>" +
            " -name \"<glob_pattern>\"");
        System.exit(-1);
    }
    ///////////////////////////////////////////////
    //
    public static List <String> lstFindFilesRecursive
    	(String sFileMask) //e.g. c:\tmp\*.*, c:\tmp\*.tmp
    			throws IOException
    {
		 List<String> lstFiles = new ArrayList<String>();
		 
		 
		 //String sFileMask = "c:\\tmp\\*.*" ;
		 File f = new File(sFileMask);
		 String sDir = f.getParent() ; //this is the directory
		 String sName = f.getName() ; //this is the file name, or wildcard
		 
		 Path pthDir = Paths.get(sDir) ;
		 String sPattern = sName ;
		
		 FileFinder.FileFindVisitor finder = new FileFinder.FileFindVisitor(sPattern);
		
		 Files.walkFileTree(pthDir, finder);
		 finder.done();
		 
		 lstFiles = finder.m_lstFiles ;
		 
		 return lstFiles ;
    }
    public static void main(String[] args) throws IOException 
    {
    	String sLog = "" ;
    	//String sMask = "c:\\tmp\\*.txt" ;
    	String sMask = "c:\\tmp\\JacksInsBack.jpeg" ;
    	List <String> lstFiles = lstFindFilesRecursive (sMask) ;
    	sLog = String.format ("%d files returned for mask %s",
    			lstFiles.size(),
    			sMask) ;
    }
}