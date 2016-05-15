package com.skipjaq.webcrawler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import clover.com.google.gson.Gson;
import clover.com.google.gson.GsonBuilder;

public class CommandLine {
    @Argument(metaVar = "URL", required = true, usage = "The application should fetch and analyze all HTML pages reachable from the URL", index = 0)
    String url;
    int deep;

    @Argument(metaVar = "DEEP", required = true, usage = "The application will fetch and analyze all HTML pages reachable up to DEPTH", index = 1)
    public void setDeep(int deep) {
        this.deep = deep;
        if (deep < 0){
            throw new RuntimeException("DEEP have to be > 0");
        }
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        CommandLine main = new CommandLine();
        CmdLineParser parser = new CmdLineParser(main);
        try {
            parser.parseArgument(args);
            main.fetchPages();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.print("Usage: <program> ");
            parser.printSingleLineUsage(System.err);
            System.err.println();
            parser.printUsage(System.err);
        }
    }

    private void fetchPages() throws NoSuchAlgorithmException, IOException {
        Collection<WebPageRecord> webPageRecords = new Webcrawler().parseRecursively(url, deep);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(webPageRecords));
    }

}
